package com.yuanyu.placardsmap;

import java.util.ArrayList;
import java.util.List;

import com.baidu.platform.comapi.basestruct.GeoPoint;

public class PlacardsLocations {
	
	public static List<GeoPoint> getGeoPoints() {
		List<GeoPoint> result = new ArrayList<GeoPoint>();
		
		result.add(new GeoPoint((int)(36.727276 * 1E6), (int)(119.141298 * 1E6)));
		result.add(new GeoPoint((int)(36.751164 * 1E6), (int)(119.158977 * 1E6)));
		result.add(new GeoPoint((int)(36.699097 * 1E6), (int)(119.231416 * 1E6)));
		result.add(new GeoPoint((int)(36.681502 * 1E6), (int)(119.227679 * 1E6)));
		result.add(new GeoPoint((int)(36.682197 * 1E6), (int)(119.236303 * 1E6)));
		result.add(new GeoPoint((int)(36.681270 * 1E6), (int)(119.102635 * 1E6)));
		result.add(new GeoPoint((int)(36.678260 * 1E6), (int)(119.131381 * 1E6)));
		result.add(new GeoPoint((int)(36.678955 * 1E6), (int)(119.209282 * 1E6)));
		result.add(new GeoPoint((int)(36.710439 * 1E6), (int)(119.176224 * 1E6)));
		
		return result;
	}
}
