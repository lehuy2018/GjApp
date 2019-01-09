package vn.toancauxanh.gg.model;

import com.google.common.base.Strings;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.sys.ValidationMessages;
import org.zkoss.bind.validator.AbstractValidator;
import org.zkoss.image.AImage;
import org.zkoss.image.Image;
import org.zkoss.io.Files;
import org.zkoss.util.media.Media;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Window;
import vn.toancauxanh.model.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

@Entity
@Table(name = "theloai")
//@SequenceGenerator(name = "per_class_gen", sequenceName = "HIBERNATE_SEQUENCE", allocationSize = 1)
public class TheLoai extends Model<TheLoai> {
    public static transient final Logger LOG = LogManager.getLogger(Image.class.getName());


    private String title = "";

    private String name = "";

    private String theloaiLink = "";

    private String description = "";

    private String extension = "";

    private String theloaiUrl = "";

    private int soThuTu;
    private Date ngayBatDau;
    private Date ngayHetHan;
    private long clickCount;
    private long viewCount;

    private boolean newTab;

    public boolean isNewTab() {
        return newTab;
    }

    public void setNewTab(boolean newTab) {
        this.newTab = newTab;
    }

    //	@SuppressWarnings("deprecation")
//	@org.hibernate.annotations.Index(name = "title")
    @Column(length = 255)
    public String getTitle() {
        return title;
    }


    public void setTitle(String title1) {
        this.title = Strings.nullToEmpty(title1);
    }

    //	@SuppressWarnings("deprecation")
//	@org.hibernate.annotations.Index(name = "description")
    @Column(length = 255)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description1) {
        this.description = Strings.nullToEmpty(description1);
    }

    //	@SuppressWarnings("deprecation")
//	@org.hibernate.annotations.Index(name = "extension")
    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension1) {
        this.extension = Strings.nullToEmpty(extension1);
    }

    //	@SuppressWarnings("deprecation")
//	@org.hibernate.annotations.Index(name = "bannerUrl")
    @Column(length = 255)
    public String getTheloaiLink() {
        return theloaiLink;
    }

    public void setTheloaiLink(String theloaiLink) {
        this.theloaiLink = Strings.nullToEmpty(theloaiLink);
    }

    public String getTheloaiUrl() {
        return theloaiUrl;
    }

    public void setTheloaiUrl(String theloaiUrl) {
        this.theloaiUrl = Strings.nullToEmpty(theloaiUrl);
    }

    //	@SuppressWarnings("deprecation")
//	@org.hibernate.annotations.Index(name = "soThuTu")
    public int getSoThuTu() {
        return soThuTu;
    }

    public void setSoThuTu(int soThuTu) {
        this.soThuTu = soThuTu;
    }

    @Command
    public void saveTheLoai(@BindingParam("list") final Object listObject,
                           @BindingParam("attr") final String attr,
                           @BindingParam("wdn") final Window wdn) throws IOException {
        setSoThuTu(0);
        save();
        wdn.detach();
        BindUtils.postNotifyChange(null, null, listObject, attr);
    }

    @Command
    public void saveBannerDoiThoai(@BindingParam("list") final Object listObject,
                                   @BindingParam("attr") final String attr,
                                   @BindingParam("wdn") final Window wdn) throws IOException {
        setSoThuTu(0);
        save();
        wdn.detach();
        BindUtils.postNotifyChange(null, null, listObject, attr);
    }


    @Transient
    public String folderTheLoaiUrl() {
        return "/" + Labels.getLabel("filestore.folder") + "/theloai/";
    }


    //	@SuppressWarnings("deprecation")
//	@org.hibernate.annotations.Index(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String _name) {
        this.name = Strings.nullToEmpty(_name);
    }

    //	@SuppressWarnings("deprecation")
//	@org.hibernate.annotations.Index(name = "bannerLink")

    @Transient
    public String getFriendlyUrl() {
        String url = "";
        if (getTheloaiLink() != null && (getTheloaiLink().contains("http://") || getTheloaiLink().contains("https://"))) {
            url = getTheloaiLink();
        } else {
            url = Executions.getCurrent().getContextPath() + getTheloaiLink();
        }
        return url;
    }

    //	@SuppressWarnings("deprecation")
//	@org.hibernate.annotations.Index(name = "ngaybatdau")
    public Date getNgayBatDau() {
        return ngayBatDau;
    }

    public void setNgayBatDau(Date ngayBatDau) {
        this.ngayBatDau = ngayBatDau == null ? null : DateUtils.setHours(DateUtils.truncate(ngayBatDau, Calendar.HOUR), 7);
    }

    //	@SuppressWarnings("deprecation")
//	@org.hibernate.annotations.Index(name = "ngayhethan")
    public Date getNgayHetHan() {
        return ngayHetHan;
    }

    public void setNgayHetHan(Date ngayHetHan) {
        this.ngayHetHan = ngayHetHan == null ? null : DateUtils.setHours(DateUtils.truncate(ngayHetHan, Calendar.HOUR), 7);
    }

    //	@SuppressWarnings("deprecation")
//	@org.hibernate.annotations.Index(name = "clickcount")
    public long getClickCount() {
        return clickCount;
    }

    public void setClickCount(long clickCount) {
        this.clickCount = clickCount;
    }

    //	@SuppressWarnings("deprecation")
//	@org.hibernate.annotations.Index(name = "viewcount")
    public long getViewCount() {
        return viewCount;
    }

    public void setViewCount(long viewCount) {
        this.viewCount = viewCount;
    }

    @Transient
    public AbstractValidator getValidatorTheLoai() {
        return new AbstractValidator() {
            @Override
            public void validate(final ValidationContext ctx) {
                Date fromDate = getNgayBatDau();
                Date toDate = getNgayHetHan();
                if (fromDate != null && toDate != null) {
                    if (fromDate.compareTo(toDate) > 0) {
                        addInvalidMessage(ctx, "lblErr",
                                "Ngày hết hạn phải lớn hơn hoặc bằng ngày bắt đầu.");
                    }
                }
            }
        };
    }
}
