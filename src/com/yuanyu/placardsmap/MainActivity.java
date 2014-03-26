package com.yuanyu.placardsmap;

import java.util.ArrayList;
import java.util.List;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.ItemizedOverlay;
import com.baidu.mapapi.map.MKMapViewListener;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.mapapi.map.PopupClickListener;
import com.baidu.mapapi.map.PopupOverlay;
import com.baidu.platform.comapi.basestruct.GeoPoint;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {

	private final static double START_LONGITUDE = 119.167313;
	private final static double START_LATITUDE = 36.713043;

	/**
	 *  MapView 是地图主控件
	 */
	private MapView mMapView = null;
	/**
	 *  用MapController完成地图控制 
	 */
	private MapController mMapController = null;
	/**
	 *  MKMapViewListener 用于处理地图事件回调
	 */
	MKMapViewListener mMapListener = null;
	
	private PlacardsLocationOverlay mOverlay = null;
	private PopupOverlay mPop = null;
	private ArrayList<OverlayItem>  mItems = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		/**
		 * 使用地图sdk前需先初始化BMapManager.
		 * BMapManager是全局的，可为多个MapView共用，它需要地图模块创建前创建，
		 * 并在地图地图模块销毁后销毁，只要还有地图模块在使用，BMapManager就不应该销毁
		 */
		CustomApplication app = (CustomApplication) getApplication();
		if (app.mBMapManager == null) {
			app.mBMapManager = new BMapManager(getApplicationContext());
			/**
			 * 如果BMapManager没有初始化则初始化BMapManager
			 */
			app.mBMapManager.init(CustomApplication.strKey,new CustomApplication.MyGeneralListener());
		}
		/**
		 * 由于MapView在setContentView()中初始化,所以它需要在BMapManager初始化之后
		 */
		setContentView(R.layout.activity_main);

		initMapView();
		initMapViewListener();
		initOverlay();
		setMapCenter();
	}

	private void initMapView() {

		mMapView = (MapView)findViewById(R.id.bmapView);
		/**
		 * 获取地图控制器
		 */
		mMapController = mMapView.getController();
		/**
		 *  设置地图是否响应点击事件  .
		 */
		mMapController.enableClick(true);
		/**
		 * 设置地图缩放级别
		 */
		mMapController.setZoom(15);
		/**
		 * 显示内置缩放控件
		 */
		mMapView.setBuiltInZoomControls(true);
	}
	
	private void setMapCenter() {
		/**
		 * 将地图移动至指定点
		 * 使用百度经纬度坐标，可以通过http://api.map.baidu.com/lbsapi/getpoint/index.html查询地理坐标
		 * 如果需要在百度地图上显示使用其他坐标系统的位置，请发邮件至mapapi@baidu.com申请坐标转换接口
		 */
		GeoPoint p ;
		Intent  intent = getIntent();
		if ( intent.hasExtra("x") && intent.hasExtra("y") ){
			//当用intent参数时，设置中心点为指定点
			Bundle b = intent.getExtras();
			p = new GeoPoint(b.getInt("y"), b.getInt("x"));
		}else{
			p = new GeoPoint((int)(START_LATITUDE * 1E6), (int)(START_LONGITUDE * 1E6));
		}

		mMapController.setCenter(p);
	}

	// Do nothing to now
	private void initMapViewListener() {
		/**
		 *  MapView的生命周期与Activity同步，当activity挂起时需调用MapView.onPause()
		 */
		mMapListener = new MKMapViewListener() {
			@Override
			public void onMapMoveFinish() {
				/**
				 * 在此处理地图移动完成回调
				 * 缩放，平移等操作完成后，此回调被触发
				 */
			}

			@Override
			public void onClickMapPoi(MapPoi mapPoiInfo) {
				/**
				 * 在此处理底图poi点击事件
				 * 显示底图poi名称并移动至该点
				 * 设置过： mMapController.enableClick(true); 时，此回调才能被触发
				 * 
				 */
				/*String title = "";
				if (mapPoiInfo != null){
					title = mapPoiInfo.strText;
					Toast.makeText(MainActivity.this, title, Toast.LENGTH_SHORT).show();
					mMapController.animateTo(mapPoiInfo.geoPt);
				}*/
			}

			@Override
			public void onGetCurrentMap(Bitmap b) {
				/**
				 *  当调用过 mMapView.getCurrentMap()后，此回调会被触发
				 *  可在此保存截图至存储设备
				 */
			}

			@Override
			public void onMapAnimationFinish() {
				/**
				 *  地图完成带动画的操作（如: animationTo()）后，此回调被触发
				 */
			}
			/**
			 * 在此处理地图载完成事件 
			 */
			@Override
			public void onMapLoadFinish() {
				Toast.makeText(MainActivity.this, "地图加载完成", Toast.LENGTH_SHORT).show();
			}
		};
		mMapView.regMapViewListener(CustomApplication.getInstance().mBMapManager, mMapListener);
	}
	
	public void initOverlay(){
    	/**
    	 * 创建自定义overlay
    	 */
         mOverlay = new PlacardsLocationOverlay(getResources().getDrawable(R.drawable.icon_mark), mMapView);	
         
         /**
          * 准备overlay 数据
          */
         List<GeoPoint> locations = PlacardsLocations.getGeoPoints();
         for(int i = 0; i < locations.size(); i++) {
        	 OverlayItem item = new OverlayItem(locations.get(i), "" + i, "");
        	 /**
              * 设置overlay图标，如不设置，则使用创建ItemizedOverlay时的默认图标.
              */
             item.setMarker(getResources().getDrawable(R.drawable.icon_mark));
        	 mOverlay.addItem(item);
         }
         
         /**
          * 保存所有item
          */
         mItems = new ArrayList<OverlayItem>();
         mItems.addAll(mOverlay.getAllItem());

         /**
          * 将overlay 添加至MapView中
          */
         mMapView.getOverlays().add(mOverlay);
         
         /**
          * 刷新地图
          */
         mMapView.refresh();
         
         /**
          * 创建一个popup overlay
          */
         PopupClickListener popListener = new PopupClickListener(){
			@Override
			public void onClickedPopup(int index) {
				// Nothing to do now, may be used in the future
			}
         };
         mPop = new PopupOverlay(mMapView, popListener);
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
    protected void onPause() {
    	/**
    	 *  MapView的生命周期与Activity同步，当activity挂起时需调用MapView.onPause()
    	 */
        mMapView.onPause();
        super.onPause();
    }
    
    @Override
    protected void onResume() {
    	/**
    	 *  MapView的生命周期与Activity同步，当activity恢复时需调用MapView.onResume()
    	 */
        mMapView.onResume();
        super.onResume();
    }
    
    @Override
    protected void onDestroy() {
    	/**
    	 *  MapView的生命周期与Activity同步，当activity销毁时需调用MapView.destroy()
    	 */
        mMapView.destroy();
        super.onDestroy();
    }
    
    @Override
    protected void onSaveInstanceState(Bundle outState) {
    	super.onSaveInstanceState(outState);
    	mMapView.onSaveInstanceState(outState);
    	
    }
    
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
    	super.onRestoreInstanceState(savedInstanceState);
    	mMapView.onRestoreInstanceState(savedInstanceState);
    }
    
    class PlacardsLocationOverlay extends ItemizedOverlay<OverlayItem>{

    	private Button mPopupButton;
    	
		public PlacardsLocationOverlay(Drawable defaultMarker, MapView mapView) {
			super(defaultMarker, mapView);
			mPopupButton = new Button(MainActivity.this);
		}

		@Override
		public boolean onTap(int index){
			OverlayItem item = getItem(index);
			mPopupButton.setText(getItem(index).getTitle());
			mPop.showPopup(mPopupButton, item.getPoint(), 32);
			return true;
		}
		
		@Override
		public boolean onTap(GeoPoint pt , MapView mMapView){
			if (mPop != null){
                mPop.hidePop();
                mMapView.removeView(mPopupButton);
			}
			return false;
		}
    }
}
