package com.example.dutmaintanenceapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class MyCustomAdapter extends ArrayAdapter<HashMap<String, Object>> {

    private final Context context;
    private final ArrayList<HashMap<String, Object>> reportData;

    public MyCustomAdapter(Context context, ArrayList<HashMap<String, Object>> reportData) {
        super(context, R.layout.list_item_report, reportData); // Set list item layout
        this.context = context;
        this.reportData = reportData;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View listViewItem = inflater.inflate(R.layout.list_item_report, parent, false);

        // Get references to TextView elements in your list item layout (list_item_report.xml)
        TextView campusTextView = listViewItem.findViewById(R.id.campusTextView);
        TextView locationTextView = listViewItem.findViewById(R.id.locationTextView);
        TextView buildingTextView = listViewItem.findViewById(R.id.buildingTextView);
        TextView issueTypeTextView = listViewItem.findViewById(R.id.issueTypeTextView);
        TextView votesTextView = listViewItem.findViewById(R.id.votesTextView);


        // Get the report data for this position
        HashMap<String, Object> report = reportData.get(position);

        // Set TextView text based on retrieved data
        String formattedCampus = "Campus: " + report.get("campus");
        String formattedLocation = "Location: " + report.get("location");
        String formattedBuilding = "Block: " + report.get("block");
        String formattedIssueType = "Issue Type: " + report.get("issueType");
        String formattedVotes = "UpVotes: " + report.get("UpVotes");

        // Set TextView text with formatted data
        campusTextView.setText(formattedCampus);
        locationTextView.setText(formattedLocation);
        buildingTextView.setText(formattedBuilding);
        issueTypeTextView.setText(formattedIssueType);
        votesTextView.setText(formattedVotes);

        return listViewItem;
    }
}
