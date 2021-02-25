package com.primesys.VehicalTracking.Dto;

public class UserModuleDTO {

	String ModuleId,Module, ModuleTitle, ModuleDesc,ImageUrl, ModuleActivity,UpdatedAt;
	public String getImageUrl() {
		return ImageUrl;
	}

	public void setImageUrl(String imageUrl) {
		ImageUrl = imageUrl;
	}

	

	public String getModuleId() {
		return ModuleId;
	}

	public void setModuleId(String moduleId) {
		ModuleId = moduleId;
	}

	public String getModule() {
		return Module;
	}

	public void setModule(String module) {
		Module = module;
	}

	public String getModuleTitle() {
		return ModuleTitle;
	}

	public void setModuleTitle(String moduleTitle) {
		ModuleTitle = moduleTitle;
	}

	public String getModuleDesc() {
		return ModuleDesc;
	}

	public void setModuleDesc(String moduleDesc) {
		ModuleDesc = moduleDesc;
	}

	public String getModuleActivity() {
		return ModuleActivity;
	}

	public void setModuleActivity(String moduleActivity) {
		ModuleActivity = moduleActivity;
	}

	public String getUpdatedAt() {
		return UpdatedAt;
	}

	public void setUpdatedAt(String updatedAt) {
		UpdatedAt = updatedAt;
	}
}
