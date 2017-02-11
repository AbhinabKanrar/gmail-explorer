/**
 * 
 */
package com.mabsisa.domain;

import lombok.Data;

/**
 * @author abhinab
 *
 */
@Data
public class Response {

	private String session_state;
	private String recovery_url;
	private String email;
	private String photo_url;
	private String name;
	private String should_redirect_in_browser_drivefs;
	private String action;
	private String shadow_email;
	private String encoded_profile_information;

}
