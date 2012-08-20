package com.lplus.tube;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

import com.lplus.facebook.LplusFacebook;
import com.lplus.widget.LplusLoginPage;



public class TubeActivity extends Activity {
	LplusFacebook facebook;
	LplusLoginPage loginPage;

	public static final String APPID = "338804906187760";
	String[] permissions = new String[] {
			// User and Friends Permissions
			  "user_about_me"
			, "user_activities"
			, "user_birthday"
			, "user_checkins"
			, "user_education_history"
			, "user_events"
			, "user_groups"
			, "user_hometown"
			, "user_interests"
			, "user_likes"
			, "user_location"
			, "user_notes"
			, "user_photos"
			, "user_questions"
			, "user_relationships"
			, "user_relationship_details"
			, "user_religion_politics"
			, "user_status"
			, "user_subscriptions"
			, "user_videos"
			, "user_website"
			, "user_work_history"
			, "email"
			// Extended Permissions
			, "read_friendlists"
			, "read_insights"
			, "read_mailbox"
			, "read_requests"
			, "read_stream"
			, "xmpp_login"
			, "ads_management"
			, "create_event"
			, "manage_friendlists"
			, "manage_notifications"
			, "user_online_presence"
			, "friends_online_presence"
			, "publish_checkins"
			, "publish_stream"
			, "rsvp_event"
			// Open Graph Permissions
			/*, "publish_actions"
			, "user_actions.music"
			, "user_actions.news"
			, "user_actions.video"
			, "user_actions:APP_NAMESPACE"
			, "user_games_activity"*/
			// Page Permissions
			, "manage_pages"
		};
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        facebook = new LplusFacebook(APPID);
      
        int numButtons = 5;
        loginPage = new LplusLoginPage(this);        
        loginPage.init(numButtons);
        for (int i = 0; i < numButtons; i++) {
        	loginPage.addLoginButton(facebook);
        }
        setContentView(loginPage);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_tube, menu);
        return true;
    }

    
}
