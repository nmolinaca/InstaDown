package com.instadown.utils;


import java.util.List;

public class Validations {

	/**
	 * Validate is null.
	 *
	 * @param o the o
	 * @return true, if successful
	 */
	public static boolean validateIsNull(Object o) {
		if (o == null) {
			return true;
		}
		return false;
	}

	/**
	 * Validate is empty.
	 *
	 * @param o the o
	 * @return true, if successful
	 */
	public static boolean validateIsEmpty(Object o) {
		String d = o.toString();
		if (d.trim().length() == 0) {
			return true;
		}
		return false;
	}

	/**
	 * Validate is not empty.
	 *
	 * @param o the o
	 * @return true, if successful
	 */
	public static boolean validateIsNotEmpty(Object o) {
		return !validateIsEmpty(o);
	}

	/**
	 * Validate is not null.
	 *
	 * @param o the o
	 * @return true, if successful
	 */
	public static boolean validateIsNotNull(Object o) {
		return !validateIsNull(o);
	}

	/**
	 * Validate is null or empty.
	 *
	 * @param o the o
	 * @return true, if successful
	 */
	public static boolean validateIsNullOrEmpty(Object o) {
		return (validateIsNull(o) || validateIsEmpty(o));
	}

	/**
	 * Validate is not null and not empty.
	 *
	 * @param o the o
	 * @return true, if successful
	 */
	public static boolean validateIsNotNullAndNotEmpty(Object o) {
		return (validateIsNotNull(o) && validateIsNotEmpty(o));
	}
	
	/**
	 * Validate list is not null and not empty.
	 *
	 * @param o the o
	 * @return true, if successful
	 */
	public static boolean validateListIsNullOrEmpty(List<?> o) {
		return (validateIsNull(o) || !validateIsPositiveNumber(o.size()));
	}

	/**
	 * Validate is positive number.
	 *
	 * @param num the num
	 * @return true, if successful
	 */
	public static boolean validateIsPositiveNumber(Integer num) {
		return num > 0;
	}
	/**
	 * Validate is either zero or positive number.
	 *
	 * @param num the num
	 * @return true, if successful
	 */
	public static boolean validateZeroOrPositiveNumber(Integer num) {
		return num >= 0;
	}
	/**
	 * Validate is positive number.
	 *
	 * @param num the num
	 * @return true, if successful
	 */
	public static boolean validateIsPositiveNumber(Long num) {
		return num > 0;
	}
	
	
	/**
	 * Validate is null or not positive.
	 *
	 * @param num the num
	 * @return true, if successful
	 */
	public static boolean validateIsNullOrNotPositive(Long num){
		return (validateIsNull(num) || !validateIsPositiveNumber(num));
	}
	
	/**
	 * Validate is not null and positive.
	 *
	 * @param num the num
	 * @return true, if successful
	 */
	public static boolean validateIsNotNullAndPositive(Long num){
		return (validateIsNotNull(num) && validateIsPositiveNumber(num));
	}
	/**
	 * Validate is null or not positive.
	 *
	 * @param num the num
	 * @return true, if successful
	 */
	public static boolean validateIsNullOrNotPositive(Integer num){
		return (validateIsNull(num) || !validateIsPositiveNumber(num));
	}
	
	/**
	 * Validate is not null and positive.
	 *
	 * @param num the num
	 * @return true, if successful
	 */
	public static boolean validateIsNotNullAndPositive(Integer num){
		return (validateIsNotNull(num) && validateIsPositiveNumber(num));
	}
	
	/**
	 * Validate list is not null and not empty.
	 *
	 * @param o the o
	 * @return true, if successful
	 */
	public static boolean validateListIsNotNullAndNotEmpty(List<?> o) {
		return (validateIsNotNull(o) && validateIsPositiveNumber(o.size()));
	}
}
