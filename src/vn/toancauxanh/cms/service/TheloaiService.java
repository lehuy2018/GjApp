package vn.toancauxanh.cms.service;

import com.querydsl.jpa.impl.JPAQuery;
import org.apache.commons.collections.MapUtils;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.util.resource.Labels;
import vn.toancauxanh.gg.model.TheLoai;
import vn.toancauxanh.gg.model.QTheLoai;
import vn.toancauxanh.service.BasicService;

import java.util.List;

public class TheloaiService extends BasicService<TheLoai> {
	
	private String img = "/backend/assets/img/edit.png";
	private String hoverImg = "/backend/assets/img/edit_hover.png";
	private String strUpdate = "Thứ tự";
	private boolean update = true;
	private boolean updateThanhCong = true;
	
	public JPAQuery<TheLoai> getTargetQuery() {
		String paramImage = MapUtils.getString(argDeco(),Labels.getLabel("param.tukhoa"),"").trim();
		String trangThai = MapUtils.getString(argDeco(),Labels.getLabel("param.trangthai"),"");
        System.out.println(paramImage);
		JPAQuery<TheLoai> q = find(TheLoai.class)
				.where(QTheLoai.theLoai.trangThai.ne(core().TT_DA_XOA));
		if (paramImage != null && !paramImage.isEmpty()) {
			String tukhoa = "%" + paramImage + "%";
			q.where(QTheLoai.theLoai.title.like(tukhoa)
				.or(QTheLoai.theLoai.description.like(tukhoa)));
            System.out.println(tukhoa);
		}
		if (!trangThai.isEmpty()) {
			q.where(QTheLoai.theLoai.trangThai.eq(trangThai));
		}
		
		q.orderBy(QTheLoai.theLoai.soThuTu.asc());
		q.orderBy(QTheLoai.theLoai.ngaySua.desc());
		return q;
	}
	
//	public JPAQuery<TheLoai> getTheLoaiDoiThoaiNho() {
//		String paramImage = MapUtils.getString(argDeco(),Labels.getLabel("param.tukhoa"),"").trim();
//		String trangThai = MapUtils.getString(argDeco(),Labels.getLabel("param.trangthai"),"");
//
//		JPAQuery<TheLoai> q = find(TheLoai.class)
//				.where(QTheLoai.theLoai.trangThai.ne(core().TT_DA_XOA));
//		if (paramImage != null && !paramImage.isEmpty()) {
//			String tukhoa = "%" + paramImage + "%";
//			q.where(QTheLoai.theLoai.title.like(tukhoa)
//				.or(QTheLoai.theLoai.description.like(tukhoa)));
//		}
//		if (!trangThai.isEmpty()) {
//			q.where(QTheLoai.theLoai.trangThai.eq(trangThai));
//		}
//		q.orderBy(QTheLoai.theLoai.soThuTu.asc());
//		q.orderBy(QTheLoai.theLoai.ngaySua.desc());
//		return q;
//	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getHoverImg() {
		return hoverImg;
	}

	public void setHoverImg(String hoverImg) {
		this.hoverImg = hoverImg;
	}

	public String getStrUpdate() {
		return strUpdate;
	}

	public void setStrUpdate(String strUpdate) {
		this.strUpdate = strUpdate;
	}

	public boolean isUpdate() {
		return update;
	}

	public void setUpdate(boolean update) {
		this.update = update;
	}

	public boolean isUpdateThanhCong() {
		return updateThanhCong;
	}

	public void setUpdateThanhCong(boolean updateThanhCong) {
		this.updateThanhCong = updateThanhCong;
	}
	
	@Command
	public void clickButton(@BindingParam("model") final List<TheLoai> model) {
		if (strUpdate.equals("Thứ tự")) {
			setStrUpdate("Lưu");
			setImg("/backend/assets/img/save.png");
			setHoverImg("/backend/assets/img/save_hover.png");
			setUpdate(false);
		} else {
			setUpdateThanhCong(true);
			for (TheLoai banner: model) {
				if (check(banner)) {
					setUpdateThanhCong(false);
					break;
				}
				banner.save();
			}
			
			if (isUpdateThanhCong()) {
				setStrUpdate("Thứ tự");
				setImg("/backend/assets/img/edit.png");
				setHoverImg("/backend/assets/img/edit_hover.png");
				setUpdate(true);
			} else {
				setUpdateThanhCong(updateThanhCong);
			}
		}
		BindUtils.postNotifyChange(null, null, this, "img");
		BindUtils.postNotifyChange(null, null, this, "hoverImg");
		BindUtils.postNotifyChange(null, null, this, "update");
		BindUtils.postNotifyChange(null, null, this, "strUpdate");
		BindUtils.postNotifyChange(null, null, this, "updateThanhCong");
		BindUtils.postNotifyChange(null, null, this, "targetQuery");
	}
	
	private boolean check (TheLoai cat) {
		if ("".equals(cat.getSoThuTu()) || cat.getSoThuTu() < 0) {
			return true;
		}
		return false;
	}
}
