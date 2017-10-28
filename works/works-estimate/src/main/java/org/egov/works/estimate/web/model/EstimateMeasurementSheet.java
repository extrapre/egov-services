package org.egov.works.estimate.web.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.egov.works.commons.domain.model.AuditDetails;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Objects;

/**
 * An Object holds the basic data of Estimate Measurement Sheet
 */
@ApiModel(description = "An Object holds the basic data of Estimate Measurement Sheet")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-10-28T12:22:31.360Z")

public class EstimateMeasurementSheet   {
  @JsonProperty("id")
  private String id = null;

  @JsonProperty("tenantId")
  private String tenantId = null;

  @JsonProperty("slNo")
  private Integer slNo = null;

  @JsonProperty("identifier")
  private String identifier = null;

  @JsonProperty("remarks")
  private String remarks = null;

  @JsonProperty("no")
  private Integer no = null;

  @JsonProperty("length")
  private Integer length = null;

  @JsonProperty("width")
  private Integer width = null;

  @JsonProperty("depthOrHeight")
  private Integer depthOrHeight = null;

  @JsonProperty("quantity")
  private Integer quantity = null;

  @JsonProperty("estimateActivity")
  private EstimateActivity estimateActivity = null;

  @JsonProperty("parent")
  private EstimateMeasurementSheet parent = null;

  @JsonProperty("auditDetails")
  private AuditDetails auditDetails = null;

  public EstimateMeasurementSheet id(String id) {
    this.id = id;
    return this;
  }

   /**
   * Unique Identifier of the Estimate Measurement Sheet
   * @return id
  **/
  @ApiModelProperty(value = "Unique Identifier of the Estimate Measurement Sheet")


  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public EstimateMeasurementSheet tenantId(String tenantId) {
    this.tenantId = tenantId;
    return this;
  }

   /**
   * Tenant id of the Estimate Measurement Sheet
   * @return tenantId
  **/
  @ApiModelProperty(required = true, value = "Tenant id of the Estimate Measurement Sheet")
  @NotNull

 @Size(min=4,max=128)
  public String getTenantId() {
    return tenantId;
  }

  public void setTenantId(String tenantId) {
    this.tenantId = tenantId;
  }

  public EstimateMeasurementSheet slNo(Integer slNo) {
    this.slNo = slNo;
    return this;
  }

   /**
   * Sl No of the Estimate Measurement sheet
   * @return slNo
  **/
  @ApiModelProperty(value = "Sl No of the Estimate Measurement sheet")


  public Integer getSlNo() {
    return slNo;
  }

  public void setSlNo(Integer slNo) {
    this.slNo = slNo;
  }

  public EstimateMeasurementSheet identifier(String identifier) {
    this.identifier = identifier;
    return this;
  }

   /**
   * Identifier of the Estimate Measurement sheet. A-Addition, D-Deduction.
   * @return identifier
  **/
  @ApiModelProperty(value = "Identifier of the Estimate Measurement sheet. A-Addition, D-Deduction.")

 @Size(max=1)
  public String getIdentifier() {
    return identifier;
  }

  public void setIdentifier(String identifier) {
    this.identifier = identifier;
  }

  public EstimateMeasurementSheet remarks(String remarks) {
    this.remarks = remarks;
    return this;
  }

   /**
   * remarks of the Measurement sheet
   * @return remarks
  **/
  @ApiModelProperty(value = "remarks of the Measurement sheet")

 @Pattern(regexp="[0-9a-zA-Z_@./#&+-/!(){}\",^$%*|=;:<>?`~ ]+") @Size(max=1024)
  public String getRemarks() {
    return remarks;
  }

  public void setRemarks(String remarks) {
    this.remarks = remarks;
  }

  public EstimateMeasurementSheet no(Integer no) {
    this.no = no;
    return this;
  }

   /**
   * No. of the Estimate Measurement sheet
   * @return no
  **/
  @ApiModelProperty(value = "No. of the Estimate Measurement sheet")


  public Integer getNo() {
    return no;
  }

  public void setNo(Integer no) {
    this.no = no;
  }

  public EstimateMeasurementSheet length(Integer length) {
    this.length = length;
    return this;
  }

   /**
   * length of the Estimate Measurement sheet
   * @return length
  **/
  @ApiModelProperty(value = "length of the Estimate Measurement sheet")


  public Integer getLength() {
    return length;
  }

  public void setLength(Integer length) {
    this.length = length;
  }

  public EstimateMeasurementSheet width(Integer width) {
    this.width = width;
    return this;
  }

   /**
   * Width of the Estimate Measurement sheet
   * @return width
  **/
  @ApiModelProperty(value = "Width of the Estimate Measurement sheet")


  public Integer getWidth() {
    return width;
  }

  public void setWidth(Integer width) {
    this.width = width;
  }

  public EstimateMeasurementSheet depthOrHeight(Integer depthOrHeight) {
    this.depthOrHeight = depthOrHeight;
    return this;
  }

   /**
   * Depth Or Height of the Estimate Measurement sheet
   * @return depthOrHeight
  **/
  @ApiModelProperty(value = "Depth Or Height of the Estimate Measurement sheet")


  public Integer getDepthOrHeight() {
    return depthOrHeight;
  }

  public void setDepthOrHeight(Integer depthOrHeight) {
    this.depthOrHeight = depthOrHeight;
  }

  public EstimateMeasurementSheet quantity(Integer quantity) {
    this.quantity = quantity;
    return this;
  }

   /**
   * Quantity of the Estimate Measurement sheet
   * @return quantity
  **/
  @ApiModelProperty(required = true, value = "Quantity of the Estimate Measurement sheet")
  @NotNull


  public Integer getQuantity() {
    return quantity;
  }

  public void setQuantity(Integer quantity) {
    this.quantity = quantity;
  }

  public EstimateMeasurementSheet estimateActivity(EstimateActivity estimateActivity) {
    this.estimateActivity = estimateActivity;
    return this;
  }

   /**
   * Estimate Activity reference for the Estimate Measurement sheet
   * @return estimateActivity
  **/
  @ApiModelProperty(required = true, value = "Estimate Activity reference for the Estimate Measurement sheet")
  @NotNull

  @Valid

  public EstimateActivity getEstimateActivity() {
    return estimateActivity;
  }

  public void setEstimateActivity(EstimateActivity estimateActivity) {
    this.estimateActivity = estimateActivity;
  }

  public EstimateMeasurementSheet parent(EstimateMeasurementSheet parent) {
    this.parent = parent;
    return this;
  }

   /**
   * Parent Estimate Measurement sheet reference for change in quantity of Revision Estimate
   * @return parent
  **/
  @ApiModelProperty(value = "Parent Estimate Measurement sheet reference for change in quantity of Revision Estimate")

  @Valid

  public EstimateMeasurementSheet getParent() {
    return parent;
  }

  public void setParent(EstimateMeasurementSheet parent) {
    this.parent = parent;
  }

  public EstimateMeasurementSheet auditDetails(AuditDetails auditDetails) {
    this.auditDetails = auditDetails;
    return this;
  }

   /**
   * Get auditDetails
   * @return auditDetails
  **/
  @ApiModelProperty(value = "")

  @Valid

  public AuditDetails getAuditDetails() {
    return auditDetails;
  }

  public void setAuditDetails(AuditDetails auditDetails) {
    this.auditDetails = auditDetails;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EstimateMeasurementSheet estimateMeasurementSheet = (EstimateMeasurementSheet) o;
    return Objects.equals(this.id, estimateMeasurementSheet.id) &&
        Objects.equals(this.tenantId, estimateMeasurementSheet.tenantId) &&
        Objects.equals(this.slNo, estimateMeasurementSheet.slNo) &&
        Objects.equals(this.identifier, estimateMeasurementSheet.identifier) &&
        Objects.equals(this.remarks, estimateMeasurementSheet.remarks) &&
        Objects.equals(this.no, estimateMeasurementSheet.no) &&
        Objects.equals(this.length, estimateMeasurementSheet.length) &&
        Objects.equals(this.width, estimateMeasurementSheet.width) &&
        Objects.equals(this.depthOrHeight, estimateMeasurementSheet.depthOrHeight) &&
        Objects.equals(this.quantity, estimateMeasurementSheet.quantity) &&
        Objects.equals(this.estimateActivity, estimateMeasurementSheet.estimateActivity) &&
        Objects.equals(this.parent, estimateMeasurementSheet.parent) &&
        Objects.equals(this.auditDetails, estimateMeasurementSheet.auditDetails);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, tenantId, slNo, identifier, remarks, no, length, width, depthOrHeight, quantity, estimateActivity, parent, auditDetails);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EstimateMeasurementSheet {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
    sb.append("    slNo: ").append(toIndentedString(slNo)).append("\n");
    sb.append("    identifier: ").append(toIndentedString(identifier)).append("\n");
    sb.append("    remarks: ").append(toIndentedString(remarks)).append("\n");
    sb.append("    no: ").append(toIndentedString(no)).append("\n");
    sb.append("    length: ").append(toIndentedString(length)).append("\n");
    sb.append("    width: ").append(toIndentedString(width)).append("\n");
    sb.append("    depthOrHeight: ").append(toIndentedString(depthOrHeight)).append("\n");
    sb.append("    quantity: ").append(toIndentedString(quantity)).append("\n");
    sb.append("    estimateActivity: ").append(toIndentedString(estimateActivity)).append("\n");
    sb.append("    parent: ").append(toIndentedString(parent)).append("\n");
    sb.append("    auditDetails: ").append(toIndentedString(auditDetails)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

