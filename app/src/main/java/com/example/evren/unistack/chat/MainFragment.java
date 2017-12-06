package com.example.evren.unistack.chat;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.evren.unistack.R;

import org.json.JSONException;
import org.json.JSONObject;
import java.net.URISyntaxException;
import java.util.ArrayList;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

/**
 * Created by EVREN on 22.10.2017.
 */


public class MainFragment extends Fragment {
    private RecyclerView.Adapter mAdapter;
    private static final int REQUEST_LOGIN = 0;
    private ArrayList<Message> messageArrayList = new ArrayList<Message>();
    private Socket mSocket;
    RecyclerView recyclerView;
    EditText mInputMessageView;
    private String mUsername,chatRoom,sinif,userName,mChatRoom;
    {
        try {
            //sunucudaki chat yazılımının url set ettik
            mSocket = IO.socket(Constants.CHAT_SERVER_URL);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
    public MainFragment() {
        super(); // fragment class metotları kullanabilmek için super metot ürettik.
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        //Mesajları listelicek olan adapter tanımladık
        mAdapter = new MessageAdapter(messageArrayList,activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
        //Socket sınıfına işlevseliği olan listener metodlarını set ettik
        mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
        mSocket.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
        mSocket.on("message", onNewMessage);
        //Socket ile bağlantı kurdum..
        mSocket.connect();
        userName= getArguments().getString("kullaniciAd");
        chatRoom = getArguments().getString("chatRoom");
        sinif = getArguments().getString("sinif");
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startSignIn();
            }
        }, 1000);//sınıf seç

    }





    //Socket ile bağlantığı kurulduğunda bir hatayla karşılasınca uyarı veren metod
    private Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getActivity().getApplicationContext(),
                            "Bağlantı Hatası3", Toast.LENGTH_LONG).show();
                }
            });
        }
    };

    //Kullanıcıya cevap olarak verilen yeni mesajları socket'den  dinleyen metod
    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    JSONObject data = (JSONObject) args[0];
                    Log.i("dfdsfsdf", String.valueOf(data));
                    String message,username;
                    try {
                        message = data.getString("mesaj");
                        username = data.getString("user");

                    } catch (JSONException e) {
                        return;
                    }


                    //Kullanıcıya cevap olarak verilen yeni mesajları ve cevap veren kullanıcıyı, mesaj listesine eklemesini sağlıyoruz
                    addMessage(username,message);
                }
            });
        }
    };

    private void  addMessage(String username,String message){
        messageArrayList.add(new Message(username,message));
        mAdapter.notifyItemInserted(messageArrayList.size()-1);
        scrollToBottom();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.main_fragment, container, false);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);

        //for crate home button
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        return view;

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.messages);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(mAdapter);
        mInputMessageView = (EditText) view.findViewById(R.id.message_input);
        ImageButton sendButton = (ImageButton) view.findViewById(R.id.send_button);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptSend();
            }
        });
    }

    //Kullanıcının girdiği mesajı  soket sınıfına set eden metod
    private void attemptSend() {
        if (null == mUsername) return;
        //Kullanıcı adı ve socket ile baglantı kontrol ediyoruz...
        if (!mSocket.connected()) return;

        //Edittext'den mesaj alındı
        String message = mInputMessageView.getText().toString().trim();
        if (TextUtils.isEmpty(message)) {
            mInputMessageView.requestFocus();
            return;
        }

        mInputMessageView.setText("");


        //Kullanıcının girdiği mesajı  soket sınıfına set ettim.
        mSocket.emit("message", message);

        addMessage(mUsername,message);
        Log.i("Liste : ", String.valueOf(messageArrayList));
    }
    private void scrollToBottom() {
        recyclerView.scrollToPosition(mAdapter.getItemCount() - 1);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();

        mSocket.disconnect();
        mSocket.off(Socket.EVENT_CONNECT_ERROR, onConnectError);
        mSocket.off(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
        mSocket.off("message", onNewMessage);
    }

    private void startSignIn() {
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.putExtra("mUsername",userName);
        intent.putExtra("chatRoom",chatRoom);
        intent.putExtra("sinif",sinif);
        startActivityForResult(intent,REQUEST_LOGIN);
    }
    private void startSignIn2() {
        sinif="5";
        Log.d("fdfsd",userName);
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.putExtra("mUsername",userName);
        intent.putExtra("chatRoom",chatRoom);
        intent.putExtra("sinif",sinif);
        startActivityForResult(intent,REQUEST_LOGIN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (Activity.RESULT_OK != resultCode) {
            getActivity().finish();
            return;
        }

        mUsername = data.getStringExtra("username");
        mChatRoom = data.getStringExtra("room");



    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
       if(id==android.R.id.home){
            startSignIn2();
        }

        return super.onOptionsItemSelected(item);
    }


}
