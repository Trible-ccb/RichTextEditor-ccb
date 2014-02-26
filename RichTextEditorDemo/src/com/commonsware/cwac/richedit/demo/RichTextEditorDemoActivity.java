/***
  Copyright (c) 2012 CommonsWare, LLC
  
  Licensed under the Apache License, Version 2.0 (the "License"); you may
  not use this file except in compliance with the License. You may obtain
  a copy of the License at
    http://www.apache.org/licenses/LICENSE-2.0
  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
*/    

package com.commonsware.cwac.richedit.demo;

import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.style.BulletSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.commonsware.cwac.richedit.BulletEffect;
import com.commonsware.cwac.richedit.ListEffect;
import com.commonsware.cwac.richedit.RichEditText;

public class RichTextEditorDemoActivity extends SherlockActivity implements OnClickListener{
  RichEditText editor=null;
  TextView htmlTextView ;
  LinearLayout mFunBarsRoot;
  
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    
    setContentView(R.layout.main);
    mFunBarsRoot = (LinearLayout) findViewById(R.id.fun_bars_root);
    int childs = mFunBarsRoot.getChildCount();
    for ( int i = 0 ; i < childs ; i++){
    	View child = mFunBarsRoot.getChildAt(i);
    	child.setOnClickListener(this);
    }
    htmlTextView = (TextView) findViewById(R.id.pre_view_html);
    editor=(RichEditText)findViewById(R.id.editor);
    editor.enableActionModes(true);
    htmlTextView.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			htmlTextView.setText(Html.toHtml(editor.getText()));
			editor.setText("");
		}
	});
    htmlTextView.setOnLongClickListener(new OnLongClickListener() {
		
		@Override
		public boolean onLongClick(View v) {
			
			editor.setText(Html.fromHtml(htmlTextView.getText().toString()));
			htmlTextView.setText("");
			editor.setText("abc\n123\n");
			Spannable s = editor.getText();
			s.setSpan(new BulletSpan(), 0, 0, 0);
			s.setSpan(new BulletSpan(), 4, 4, 0);
			s.setSpan(new BulletSpan(), 8, 8, 0);
			return false;
		}
	});
  }

  	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.bold:
				editor.toggleEffect(RichEditText.BOLD);
				
				break;
			case R.id.insert_img:
				
				break;
			case R.id.italic:
				editor.toggleEffect(RichEditText.ITALIC);
				break;
			case R.id.underline:
				editor.toggleEffect(RichEditText.UNDERLINE);
				break;
			case R.id.bullet:
				editor.toggleEffect(BulletEffect.getInstance());
				break;
			case R.id.number_list:
				editor.toggleEffect(ListEffect.getInstance());
				break;
			default:
				break;
		}
	}
}
