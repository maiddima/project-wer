package maiddima.imperfect.wer.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import maiddima.imperfect.wer.R;

public class MessageAdapter extends BaseAdapter {

    List<Message> messages = new ArrayList<Message>();
    Context context;

    public MessageAdapter(Context context) {
        this.context = context;
    }

    public void add(Message message) {
        this.messages.add(message);
    }

    public void clear() {
        messages.clear();
    }

    @Override
    public int getCount() { return messages.size(); }

    @Override
    public Object getItem(int i) { return messages.get(i); }

    @Override
    public long getItemId(int i) { return i; }

    // This is the backbone of the class, it handles the creation of single ListView row (chat bubble)
    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {

        MessageViewHolder holder = new MessageViewHolder();
        Message message = messages.get(i);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (message.getStudent_id() == 33173463) {
            convertView = inflater.inflate(R.layout.my_message, null);
            holder.message = (TextView) convertView.findViewById(R.id.tv_message);
            holder.student_id = (TextView) convertView.findViewById(R.id.tv_studentID);
            holder.gpsLat = (TextView) convertView.findViewById(R.id.tv_gpsLat);
            holder.gpsLong = (TextView) convertView.findViewById(R.id.tv_gpsLong);

            convertView.setTag(holder);

            holder.message.setText(message.getStudent_message());
            holder.student_id.setText(Integer.toString(message.getStudent_id()));

            double lat = message.getGps_lat();
            String stringLat = Double.toString(lat);
            holder.gpsLat.setText(stringLat);

            double lon = message.getGps_long();
            String stringLong = Double.toString(lon);
            holder.gpsLong.setText(stringLong);

        }else{
            convertView = inflater.inflate(R.layout.others_message, null);
            holder.message = (TextView) convertView.findViewById(R.id.tv_message);
            holder.student_id = (TextView) convertView.findViewById(R.id.tv_studentID);
            holder.gpsLat = (TextView) convertView.findViewById(R.id.tv_gpsLat);
            holder.gpsLong = (TextView) convertView.findViewById(R.id.tv_gpsLong);

            convertView.setTag(holder);

            holder.message.setText(message.getStudent_message());
            holder.student_id.setText(Integer.toString(message.getStudent_id()));

            double lat = message.getGps_lat();
            String stringLat = Double.toString(lat);
            holder.gpsLat.setText(stringLat);

            double lon = message.getGps_long();
            String stringLong = Double.toString(lon);
            holder.gpsLong.setText(stringLong);
        }

        return convertView;


    }

    class MessageViewHolder {
        public TextView student_id;
        public TextView message;
        public TextView gpsLat;
        public TextView gpsLong;
    }

}
