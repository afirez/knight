package com.afirez.app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Matrix;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.disposables.Disposable;

public class RxTouchImageView extends AppCompatImageView {

    public RxTouchImageView(Context context) {
        super(context);
        init();
    }

    public RxTouchImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RxTouchImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        Matrix matrix = new Matrix();
        setScaleType(ScaleType.MATRIX);
        setImageMatrix(matrix);

        @SuppressLint("ClickableViewAccessibility")
        Observable<Event> touchStream = Observable.create((ObservableEmitter<Event> emitter) -> {
            setOnTouchListener((v, event) -> {
                int pointerCount = event.getPointerCount();
                if (pointerCount == 1) {
                    Event e = new Event();
                    e.action = event.getActionMasked();
                    e.p1 = new Vector(event.getX(), event.getY());
                    emitter.onNext(e);
                } else if (pointerCount == 2) {
                    Event e = new Event();
                    e.action = event.getActionMasked();
                    e.p1 = new Vector(event.getX(0), event.getY(0));
                    e.p2 = new Vector(event.getX(1), event.getY(1));
                    emitter.onNext(e);
                }
                return true;
            });
        }).share();

        Observable<Event> pointer1Down = touchStream.filter(e -> e.action == MotionEvent.ACTION_DOWN);
        Observable<Event> pointer2Down = touchStream.filter(e -> e.action == MotionEvent.ACTION_POINTER_DOWN);
        Observable<Event> pointerMove = touchStream.filter(e -> e.action == MotionEvent.ACTION_MOVE);
        Observable<Event> pointer2Up = touchStream.filter(e -> e.action == MotionEvent.ACTION_POINTER_UP);
        Observable<Event> pointer1Up = touchStream.filter(e -> e.action == MotionEvent.ACTION_UP);

        Observable<Vector> delta1 = Observable.combineLatest(pointerMove, pointerMove.skip(1), (prev, cur) -> prev.p1.subtract(cur.p1));
        Observable<Delta> delta2 = Observable.combineLatest(pointerMove, pointerMove.skip(1), Delta::delta);

        Disposable disposable = pointer1Down
                .mergeWith(pointer2Up)
                .flatMap(e -> delta1.takeUntil(pointer1Up).takeUntil(pointer2Down))
                .subscribe(v -> {
                    matrix.postTranslate(v.x, v.y);
                    setImageMatrix(matrix);
                });

        Disposable disposable1 = pointer2Down
                .flatMap(e -> delta2.takeUntil(pointer2Up))
                .subscribe(d -> {
                    matrix.postTranslate(d.translate.x, d.translate.y);
                    matrix.postRotate(d.rotate, d.center.x, d.center.y);
                    matrix.postScale(d.scale, d.scale, d.center.x, d.center.y);
                    setImageMatrix(matrix);
                });
    }


    static class Delta {
        Vector center;
        Vector translate;
        float scale;
        float rotate;

        static Delta delta(Event prev, Event cur) {
            Delta delta = new Delta();
            delta.center = cur.center();
            delta.translate = prev.center().subtract(cur.center());
            delta.scale = prev.length() / cur.length();
            delta.rotate = cur.vector().angle(prev.vector());
            return delta;
        }
    }


    static class Event {
        int action;
        Vector p1, p2;

        Vector center() {
            return p1.add(p2).multiply(0.5f);
        }

        float length() {
            return p1.subtract(p2).length();
        }

        Vector vector() {
            return p1.subtract(p2);
        }
    }


    static class Vector {
        float x, y;

        Vector(float x, float y) {
            this.x = x;
            this.y = y;
        }

        Vector add(Vector v) {
            return new Vector(x + v.x, y + v.y);
        }

        Vector subtract(Vector v) {
            return new Vector(x - v.x, y - v.y);
        }

        Vector multiply(float s) {
            return new Vector(x * s, y * s);
        }

        float dot(Vector v) {
            return x * v.x + y * v.y;
        }

        float cross(Vector v) {
            return x * v.y - y * v.x;
        }

        float length() {
            return (float) Math.sqrt(x * x + y * y);
        }

        float angle(Vector b) {
            float cos = dot(b) / (length() * b.length());
            cos = Math.max(-1.0f, Math.min(1.0f, cos));
            float degree = (float) Math.toDegrees(Math.acos(cos));
            return cross(b) > 0 ? degree : -degree;
        }
    }

}