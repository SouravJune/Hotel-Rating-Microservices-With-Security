package com.ms.hotel.service;

import java.util.List;

import com.ms.hotel.entity.Hotel;
import com.ms.hotel.response.HotelResponse;

public interface HotelService {

	Hotel saveHotel(Hotel hotel);
	List<HotelResponse> getAllHotels();
	HotelResponse getHotel(String hotelId);
	Hotel updateHotel(String hotelId, Hotel hotel);
	String deleteHotel(String hotelId);

}
