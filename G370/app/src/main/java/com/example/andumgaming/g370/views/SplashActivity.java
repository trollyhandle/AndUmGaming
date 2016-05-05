package com.example.andumgaming.g370.views;

/**
 * Created by Jeff on 4/21/2016.


public class SplashActivity extends Activity implements BackStackLisnter {


    public SplashActivity(){

    }

    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance){

        View view = inflater.inflate(R.layout.splash,container, false);

        final ImageView iv = (ImageView)view.findViewById(R.id.sanic);
        final Animation an = AnimationUtils.loadAnimation(this,R.anim.rotate);

        iv.startAnimation(an);
        an.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Intent i = new Intent(SplashActivity.this, Login);
                SplashActivity.this.startActivity( i );
            }


        });
        return view;
    }
}
*/