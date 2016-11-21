package com.zhouning.myfirstprog;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.zhouning.myfirstprog.view.FeatureView;

public class Entry extends ListActivity {

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);
        ListAdapter adapter = new CustomArrayAdapter(
                this.getApplicationContext(), demos);
        setListAdapter(adapter);
    }

    private static class DemoDetails {
        private final String titleId;
        private final String descriptionId;
        private final Class<? extends android.app.Activity> activityClass;

        public DemoDetails(String titleId, String descriptionId,
                           Class<? extends android.app.Activity> activityClass) {
            super();
            this.titleId = titleId;
            this.descriptionId = descriptionId;
            this.activityClass = activityClass;
        }
    }

    private static class CustomArrayAdapter extends ArrayAdapter<DemoDetails> {
        public CustomArrayAdapter(Context context, DemoDetails[] demos) {
            super(context, R.layout.feature, R.id.title, demos);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            FeatureView featureView;
            if (convertView instanceof FeatureView) {
                featureView = (FeatureView) convertView;
            } else {
                featureView = new FeatureView(getContext());
            }
            DemoDetails demo = getItem(position);
            featureView.setTitleId(demo.titleId);
            featureView.setDescriptionId(demo.descriptionId);
            return featureView;
        }
    }

    private static final DemoDetails[] demos = {
            new DemoDetails("WebFragment", "automatic add fragment", FragmentAddWebView.class),
            new DemoDetails("Share", "Share Test", FingerPrintTest.class),
//            new DemoDetails("H5", "H5 Test", H5.class),
            new DemoDetails("Menu", "Menu Test", MenuTest.class),
            new DemoDetails("Load HTML", "Load HTML Page", SecondActivity.class),
            new DemoDetails("SSL", "SSL Function Test", SSLPageTest.class),
            new DemoDetails("UnZip", "Zip and Unzip files", UnzipTest.class),
            new DemoDetails("Read File", "Read Json Files from Phone", ReadJsonFile.class),
//            new DemoDetails("Download", "Download zip file from server", ReadJsonFile.class),
            new DemoDetails("Encrypt", "Encrypt Str", EncryptTest.class),};
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        DemoDetails demo = (DemoDetails) getListAdapter().getItem(position);
        startActivity(new Intent(this.getApplicationContext(),
                demo.activityClass));
    }
}
