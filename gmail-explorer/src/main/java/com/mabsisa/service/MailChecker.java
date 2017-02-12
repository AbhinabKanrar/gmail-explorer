/**
 * 
 */
package com.mabsisa.service;

import com.mabsisa.Application;
import com.mabsisa.util.CommonConstant;

import reactor.bus.Event;

/**
 * @author abhinab
 *
 */
public class MailChecker {

	public static void check(String search, int length) {
		if (search != null && !search.trim().isEmpty()) {
			char[] sarchBase = search.toCharArray();
			possibleMails(length, sarchBase, "");
		}
	}

	public static void possibleMails(int maxLength, char[] sarchBase, String currMail) {
		if (currMail.length() == maxLength) {
			Application.getEventBus().notify(CommonConstant.CONSUMER_KEY, Event.wrap(currMail));
		} else {
			for (char item : sarchBase) {
				String oldMail = currMail;
				currMail += item;
				possibleMails(maxLength, sarchBase, currMail);
				currMail = oldMail;
			}
		}
	}

}
