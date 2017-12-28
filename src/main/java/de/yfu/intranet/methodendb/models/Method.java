package de.yfu.intranet.methodendb.models;

import static de.yfu.intranet.methodendb.models.Method.METHOD_TABLE;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

import de.yfu.intranet.methodendb.dtos.MethodResource;

@Entity
@Table(name=METHOD_TABLE)
public class Method implements Serializable {

	private static final long serialVersionUID = 1104549652651553182L;
	public static final String METHOD_TABLE = "mm_method";
	
	private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSz";

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="mm_id")
	private UUID id;
	
	@Column(name="mm_title")
	private String title;
	
	@Column(name="mm_content")
	private String content;
	
	@OneToMany(mappedBy="method", cascade=CascadeType.PERSIST)
	@Column(name="mm_attachment_id")
	private Set<Attachment> attachments;
	
	@ManyToMany(fetch=FetchType.EAGER, cascade=CascadeType.MERGE)
	@JoinTable(
			name="mm_method_method_level",
			joinColumns=@JoinColumn(name="mm_method_id", referencedColumnName="mm_id"),
			inverseJoinColumns=@JoinColumn(name="mm_method_level_id", referencedColumnName="ml_id"))
	private Set<MethodLevel> methodLevels;
	
	@ManyToMany(cascade=CascadeType.MERGE)
	@JoinTable(
			name="mm_method_method_type",
			joinColumns=@JoinColumn(name="mm_method_id", referencedColumnName="mm_id"),
			inverseJoinColumns=@JoinColumn(name="mm_method_type_id", referencedColumnName="mt_id"))
	private Set<MethodType> methodTypes;

	@ManyToMany
	@JoinTable(name="mm_method_seminar_goal",
			joinColumns=@JoinColumn(name="mm_method_id", referencedColumnName="mm_id"),
			inverseJoinColumns=@JoinColumn(name="mm_seminar_goal_id", referencedColumnName="sg_id"))
	private Set<SeminarGoal> seminarGoals;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_FORMAT)
	@Column(name="mm_created_at")
	private Date createdAt;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_FORMAT)
	@Column(name="mm_modified_at")
	private Date modifiedAt;
	
	@ManyToOne//(cascade=CascadeType.MERGE)
	@JoinColumn(name="mm_created_by")
	private User createdBy;
	
	@ManyToOne// (cascade=CascadeType.MERGE)
	@JoinColumn(name="mm_modified_by")
	private User modifiedBy;

	public Method() {
		super();
	}

	public Method(String title, String content, Set<MethodLevel> methodLevels, Set<MethodType> methodTypes,
			Set<SeminarGoal> seminarGoals, Set<Attachment> attachments, User createdBy, User modifiedBy) {
		super();
		this.title = title;
		this.content = content;
		this.methodLevels = methodLevels;
		this.methodTypes = methodTypes;
		this.seminarGoals = seminarGoals;
		this.attachments = attachments;
		this.createdBy = createdBy;
		this.modifiedBy = modifiedBy;
	}
	
	public Method(UUID userId, MethodResource methodResource) {
		super();
		this.title = methodResource.getTitle();
		this.content = methodResource.getContent();
		this.attachments = methodResource.getAttachments();
		this.methodLevels = methodResource.getMethodLevels();
		this.methodTypes = methodResource.getMethodTypes();
		this.seminarGoals = methodResource.getSeminarGoals();
		
	}

	public User getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(User modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Set<Attachment> getAttachments() {
		return attachments;
	}

	public void setAttachments(Set<Attachment> attachments) {
		this.attachments = attachments;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Set<MethodLevel> getMethodLevels() {
		return methodLevels;
	}

	public void setMethodLevels(Set<MethodLevel> methodLevels) {
		this.methodLevels = methodLevels;
	}

	public Set<MethodType> getMethodTypes() {
		return methodTypes;
	}

	public void setMethodTypes(Set<MethodType> methodTypes) {
		this.methodTypes = methodTypes;
	}

	public Set<SeminarGoal> getSeminarGoals() {
		return seminarGoals;
	}

	public void setSeminarGoals(Set<SeminarGoal> seminarGoals) {
		this.seminarGoals = seminarGoals;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getModifiedAt() {
		return modifiedAt;
	}

	public void setModifiedAt(Date modifiedAt) {
		this.modifiedAt = modifiedAt;
	}

	public User getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(User createdBy) {
		this.createdBy = createdBy;
	}
	
	@PrePersist
	protected void onCreate() {
		createdAt = new Date();
	}
	
	@PreUpdate
	protected void onUpdate() {
		modifiedAt = new Date();
	}
}