package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.egov.works.commons.domain.model.AuditDetails;
import org.egov.works.estimate.web.model.Asset;
import org.egov.works.estimate.web.model.DetailedEstimate;

import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * An Object holds the basic data for Assets for Estimate
 */
@ApiModel(description = "An Object holds the basic data for Assets for Estimate")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-10-28T12:22:31.360Z")

public class AssetsForEstimate   {
    @JsonProperty("id")
    private String id = null;

    @JsonProperty("tenantId")
    private String tenantId = null;

    @JsonProperty("asset")
    private Asset asset = null;

    @JsonProperty("detailedEstimate")
    private DetailedEstimate detailedEstimate = null;

    @JsonProperty("auditDetails")
    private AuditDetails auditDetails = null;

    public AssetsForEstimate id(String id) {
        this.id = id;
        return this;
    }

    /**
     * Unique Identifier of the Assets For Estimate
     * @return id
     **/
    @ApiModelProperty(value = "Unique Identifier of the Assets For Estimate")


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public AssetsForEstimate tenantId(String tenantId) {
        this.tenantId = tenantId;
        return this;
    }

    /**
     * Tenant id of the Assets For Estimate
     * @return tenantId
     **/
    @ApiModelProperty(required = true, value = "Tenant id of the Assets For Estimate")
    @NotNull

    @Size(min=4,max=128)
    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public AssetsForEstimate asset(Asset asset) {
        this.asset = asset;
        return this;
    }

    /**
     * Asset Referenvce from Asset module
     * @return asset
     **/
    @ApiModelProperty(required = true, value = "Asset Referenvce from Asset module")
    @NotNull

    @Valid

    public Asset getAsset() {
        return asset;
    }

    public void setAsset(Asset asset) {
        this.asset = asset;
    }

    public AssetsForEstimate detailedEstimate(DetailedEstimate detailedEstimate) {
        this.detailedEstimate = detailedEstimate;
        return this;
    }

    /**
     * Reference of the Detailed Estimate for Estimate and Assets linking
     * @return detailedEstimate
     **/
    @ApiModelProperty(required = true, value = "Reference of the Detailed Estimate for Estimate and Assets linking")
    @NotNull

    @Valid

    public DetailedEstimate getDetailedEstimate() {
        return detailedEstimate;
    }

    public void setDetailedEstimate(DetailedEstimate detailedEstimate) {
        this.detailedEstimate = detailedEstimate;
    }

    public AssetsForEstimate auditDetails(AuditDetails auditDetails) {
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
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AssetsForEstimate assetsForEstimate = (AssetsForEstimate) o;
        return Objects.equals(this.id, assetsForEstimate.id) &&
                Objects.equals(this.tenantId, assetsForEstimate.tenantId) &&
                Objects.equals(this.asset, assetsForEstimate.asset) &&
                Objects.equals(this.detailedEstimate, assetsForEstimate.detailedEstimate) &&
                Objects.equals(this.auditDetails, assetsForEstimate.auditDetails);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tenantId, asset, detailedEstimate, auditDetails);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class AssetsForEstimate {\n");

        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
        sb.append("    asset: ").append(toIndentedString(asset)).append("\n");
        sb.append("    detailedEstimate: ").append(toIndentedString(detailedEstimate)).append("\n");
        sb.append("    auditDetails: ").append(toIndentedString(auditDetails)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces
     * (except the first line).
     */
    private String toIndentedString(java.lang.Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }
}
