package com.commonsware.cwac.richedit;

import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.BulletSpan;
import android.util.Log;
import android.widget.Toast;

public class BulletEffect extends Effect<Boolean> {

	public static final String TAG = "BULLET_EFFECT";
	
	private static BulletEffect instance = null;
	
	public static BulletEffect getInstance(){
		if ( instance == null ){
			instance = new BulletEffect();
		}
		return instance;
	}
	@Override
	boolean existsInSelection(RichEditText editor) {
		return valueInSelection(editor);
	}

	@Override
	Boolean valueInSelection(RichEditText editor) {
		Selection selection = new Selection(editor);
		BulletSpan[] spans = getBulletSpans(selection.start, selection.end, editor);
		if ( spans.length > 0 )return true;
		return false;
	}

	@Override
	void applyToSelection(RichEditText editor, Boolean add) {
		Selection selection = new Selection(editor);

		int start = selection.start;
		int end = selection.end;
		
		int leftEdge = findLineLeftEdgeIndexAtSelection(start, editor);
		int rightEdge = findLineRightEdgeIndexAtSelection(end, editor);
		
		BulletSpan[] spans = getBulletSpansOnSelectionLine(selection, editor);
		boolean needAdd = (spans.length == 0);
		Toast.makeText(editor.getContext(), "lf = " + leftEdge + " right = " + rightEdge,
				Toast.LENGTH_LONG).show();
		if ( needAdd ){
			String sb = editor.getText().subSequence(leftEdge, rightEdge).toString();
			String[] sps = sb.split("\n");
			int curs = leftEdge;
			for ( int i = 0 ; i < sps.length ; i++ ){
				editor.getText().setSpan(new MyBulletSpan(), 
						curs, curs + sps[i].length(), 0);
				curs += sps[i].length() + 1;
			}
			editor.registerKeyObserver(this);
		} else {
			for ( BulletSpan span : spans ){
				editor.getText().removeSpan(span);
			}
			editor.removeKeyObserver(this);
		}
	}

	
	int findLineLeftEdgeIndexAtSelection (int start,RichEditText editor) {
		
		CharSequence content = editor.getText();
		if ( TextUtils.isEmpty(content) )return 0;
		int idx = Math.min(content.length() - 1 , start -1);
		while (idx >= 0 && idx < content.length()){
			char c = content.charAt(idx);
			if ( c == '\n' ){
				Log.i(TAG, "left edge idx = " + (idx + 1));
				return idx + 1;
			}
			idx--;

		}
		if ( idx < 0 ){
			idx = 0;
		}
		return idx ;
	}
	int findLineRightEdgeIndexAtSelection (int end,RichEditText editor) {
		
		CharSequence content = editor.getText();
		if ( TextUtils.isEmpty(content) )return 0;
		if ( end >= content.length() )return content.length();
		
		int idx = Math.min(content.length() - 1 , end);
		while (idx >= 0 && idx < content.length() ){
			char c = content.charAt(idx);
			if ( c == '\n' ){
				Log.i(TAG, "right edge idx = " + (idx));
				return idx;
			}
			idx++;
		}
		if ( idx >= content.length() ){
			idx = content.length() ;
		}
		return idx ;
	}
	private BulletSpan[] getBulletSpans(int s,int e,RichEditText editor){
		return editor.getText().getSpans(s, e, MyBulletSpan.class);
	}
	private BulletSpan[] getBulletSpansOnSelectionLine(Selection selection,RichEditText editor){
		int start = selection.start;
		int end = selection.end;
		int leftEdge = findLineLeftEdgeIndexAtSelection(start, editor);
		int rightEdge = findLineRightEdgeIndexAtSelection(end, editor);
		return editor.getText().getSpans(leftEdge, rightEdge, MyBulletSpan.class);
	}
	@Override
	public void onKeyEnter(RichEditText editor) {
		super.onKeyEnter(editor);
		Selection selection = new Selection(editor);
		int leftEdge = selection.start;
		int rightEdge = findLineRightEdgeIndexAtSelection(selection.end, editor);
		BulletSpan[] spans = getBulletSpans(selection.start - 1, selection.end - 1, editor);
		if ( spans.length == 1 ){
			int spanStart = editor.getText().getSpanStart(spans[0]);
			int spansEnd = editor.getText().getSpanEnd(spans[0]);
			editor.getText().removeSpan(spans[0]);
			editor.getText().setSpan(new MyBulletSpan(), 
					spanStart, leftEdge - 1, 0);
			editor.getText().setSpan(new MyBulletSpan(), 
						leftEdge, rightEdge, 0);
		}
		Toast.makeText(editor.getContext(), "lf = " + leftEdge + " right = " + rightEdge,
				Toast.LENGTH_LONG).show();
	}
	
	class MyBulletSpan extends BulletSpan{
		public MyBulletSpan(){
			super(10);
		}
	}
}
