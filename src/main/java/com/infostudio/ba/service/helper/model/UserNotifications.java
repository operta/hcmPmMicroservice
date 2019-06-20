package com.infostudio.ba.service.helper.model;

import java.io.Serializable;
import java.time.Instant;

public class UserNotifications implements Serializable {
	private Long id;

    private Long id_job_application;

    private String is_read;

    private Long idUser;

    private Long notification_templatesId;

    private String createdBy;

    private Instant createdAt;

    private String updatedBy;

    private Instant updatedAt;

    private String notificationData;

    public UserNotifications() {}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId_job_application() {
		return id_job_application;
	}

	public void setId_job_application(Long id_job_application) {
		this.id_job_application = id_job_application;
	}

	public String getIs_read() {
		return is_read;
	}

	public void setIs_read(String is_read) {
		this.is_read = is_read;
	}

	public Long getIdUser() {
		return idUser;
	}

	public void setIdUser(Long idUser) {
		this.idUser = idUser;
	}

	public Long getNotification_templatesId() {
		return notification_templatesId;
	}

	public void setNotification_templatesId(Long notification_templatesId) {
		this.notification_templatesId = notification_templatesId;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Instant createdAt) {
		this.createdAt = createdAt;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Instant getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Instant updatedAt) {
		this.updatedAt = updatedAt;
	}

	public String getNotificationData() {
		return notificationData;
	}

	public void setNotificationData(String notificationData) {
		this.notificationData = notificationData;
	}

	@Override
	public String toString() {
		return "UserNotifications{" +
				"id=" + id +
				", id_job_application=" + id_job_application +
				", is_read='" + is_read + '\'' +
				", idUser=" + idUser +
				", notification_templatesId=" + notification_templatesId +
				", createdBy='" + createdBy + '\'' +
				", createdAt=" + createdAt +
				", updatedBy='" + updatedBy + '\'' +
				", updatedAt=" + updatedAt +
				", notificationData='" + notificationData + '\'' +
				'}';
	}
}
