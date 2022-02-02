package com.bbsi.platform.user.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.Data;


/**
 * @author rneelati
 *
 */
@Data
@Entity
@Table(name = "User_Toolbar_Settings", uniqueConstraints = @UniqueConstraint(columnNames = { "CLIENT_CODE","USER_EMAIL", "MENU_ITEM_ID", "FEATURE_CODE"}))
public class UserToolbarSettings extends EntityBase {
	
	private static final long serialVersionUID = -530196361073551606L;

	@Id
    @SequenceGenerator(name = "user_toolbar_settings_gen", sequenceName = "user_toolbar_settings_id_seq", allocationSize = 1, initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_toolbar_settings_gen")
    @Column(name = "ID")
	private long id;

	@Column(name="USER_EMAIL")
	private String userEmail;

	@Column(name="CLIENT_CODE")
	private String clientCode;
	
	@Column(name="MENU_ITEM_ID")
	private long menuItemId;
	
	@Column(name="FEATURE_CODE")
	private String featureCode;

}
