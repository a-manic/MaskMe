package edu.ucsb.ece150.maskme;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.util.SparseArray;

import androidx.appcompat.widget.AppCompatImageView;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.Landmark;
import edu.ucsb.ece150.maskme.camera.GraphicOverlay.Graphic;

import java.util.List;

import edu.ucsb.ece150.maskme.FaceTrackerActivity.MaskType;

import static edu.ucsb.ece150.maskme.camera.GraphicOverlay.Graphic.scaleX;
import static edu.ucsb.ece150.maskme.camera.GraphicOverlay.Graphic.scaleY;
import static edu.ucsb.ece150.maskme.camera.GraphicOverlay.Graphic.translateX;
import static edu.ucsb.ece150.maskme.camera.GraphicOverlay.Graphic.translateY;

public class MaskedImageView extends AppCompatImageView {

    private SparseArray<Face> faces = null;
    private MaskType maskType = MaskType.FIRST;
    Paint mPaint = new Paint();
    private Bitmap mBitmap;

    public MaskedImageView(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mBitmap = ((BitmapDrawable) getDrawable()).getBitmap();
        if(mBitmap == null){
            return;
        }
        double viewWidth = canvas.getWidth();
        double viewHeight = canvas.getHeight();
        double imageWidth = mBitmap.getWidth();
        double imageHeight = mBitmap.getHeight();
        double scale = Math.min(viewWidth / imageWidth, viewHeight / imageHeight);

        drawBitmap(canvas, scale);

        switch (maskType){
            case FIRST:
                drawFirstMaskOnCanvas(canvas, scale);
                break;
            case SECOND:
                drawSecondMaskOnCanvas(canvas, scale);
                break;
        }
    }

    protected void drawMask(SparseArray<Face> faces, MaskType maskType){
        this.faces = faces;
        this.maskType = maskType;
        this.invalidate();
    }

    private void drawBitmap(Canvas canvas, double scale) {
        double imageWidth = mBitmap.getWidth();
        double imageHeight = mBitmap.getHeight();

        Rect destBounds = new Rect(0, 0, (int)(imageWidth * scale), (int)(imageHeight * scale));
        canvas.drawBitmap(mBitmap, null, destBounds, null);
    }

    private void drawFirstMaskOnCanvas(Canvas canvas, double scale) {

        Bitmap mask;
        float deltaX, deltaY;
        for ( int i =0; i<faces.size(); i++) {
            Face face = faces.valueAt(i);

            float x = translateX(face.getPosition().x + face.getWidth() / 2);
            float y = translateY(face.getPosition().y + face.getHeight() / 2);
            deltaX = 0.8f * scaleX(face.getWidth() / 2);
            deltaY = 1.2f * scaleY(face.getHeight() / 2);

            mask = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.mask1);
            Rect destBounds = new Rect((int)(x - deltaX), (int)(y - deltaY), (int)(x + deltaX), (int)(y + deltaY));
            canvas.drawBitmap(mask, null, destBounds, null);
        }


        /*int left=0, top=0, right=0, bottom=0;


        // [TODO] Draw first type of mask on the static photo
        for ( int i =0; i<faces.size(); i++){
            Face face = faces.valueAt(i);

            List<Landmark> landmarks = face.getLandmarks();
            mask = BitmapFactory.decodeResource(getContext().getResources(),R.drawable.mask1);
            left = (int) translateX(landmarks.get(3).getPosition().x);
            top = (int) translateY(landmarks.get(6).getPosition().y);
            right = (int) translateX(landmarks.get(9).getPosition().x);
            bottom = (int) translateY(landmarks.get(0).getPosition().y);

            Rect destBounds = new Rect(left, top, right,bottom);
            canvas.drawBitmap(mask, null, destBounds, null);

        }*/

    }

    private void drawSecondMaskOnCanvas( Canvas canvas, double scale ) {
        // [TODO] Draw second type of mask on the static photo

        Bitmap mask;
        float deltaX, deltaY;
        for ( int i =0; i<faces.size(); i++) {
            Face face = faces.valueAt(i);

            float x = translateX(face.getPosition().x + face.getWidth() / 2);
            float y = translateY(face.getPosition().y + face.getHeight() / 2);
            deltaX = 0.8f * scaleX(face.getWidth() / 2);
            deltaY = 1.2f * scaleY(face.getHeight() / 2);

            mask = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.mask1);
            Rect destBounds = new Rect((int)(x - deltaX), (int)(y - deltaY), (int)(x + deltaX), (int)(y + deltaY));
            canvas.drawBitmap(mask, null, destBounds, null);
        }
        /*Bitmap mask;
        int left=0, top=0, right=0, bottom=0;

        for ( int i =0; i<faces.size(); i++){
            Face face = faces.valueAt(i);

            List<Landmark> landmarks = face.getLandmarks();
            mask = BitmapFactory.decodeResource(getContext().getResources(),R.drawable.mask2);
            left = (int) translateX(landmarks.get(3).getPosition().x);
            top = (int) translateY(landmarks.get(6).getPosition().y);
            right = (int) translateX(landmarks.get(9).getPosition().x);
            bottom = (int) translateY(landmarks.get(0).getPosition().y);

            Rect destBounds = new Rect(left, top, right,bottom);
            canvas.drawBitmap(mask, null, destBounds, null);

        }*/


    }

    public void noFaces() {
        faces = null;
    }

    public void reset() {
        faces = null;
        setImageBitmap(null);
    }
}
