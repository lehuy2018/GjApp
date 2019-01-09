package vn.toancauxanh.cms.service;

import com.querydsl.jpa.impl.JPAQuery;
import org.apache.commons.collections.MapUtils;
import org.zkoss.util.resource.Labels;
import vn.toancauxanh.gg.model.*;
import vn.toancauxanh.service.BasicService;

import java.util.List;

public class HinhAnhService extends BasicService<HinhAnh> {

	private String strUpdate = "Thứ tự";
	private String img = "/backend/assets/img/edit.png";
	private String hoverImg = "/backend/assets/img/edit_hover.png";
	private boolean update = true;
	private boolean updateThanhCong = true;
	
	public JPAQuery<HinhAnh> getTargetQuery() {
		long chude = MapUtils.getLongValue(argDeco(), Labels.getLabel("param.category"));
		String paramTuKhoa = MapUtils.getString(argDeco(), Labels.getLabel("param.tukhoa"), "").trim();
		long tacGia = MapUtils.getLongValue(argDeco(), Labels.getLabel("param.tacgia"));
		String paramTrangThaiNoiBat= MapUtils.getString(argDeco(), Labels.getLabel("param.trangthainoibat"), "");
		String paramTrangThaiSoan = MapUtils.getString(argDeco(), Labels.getLabel("param.trangthaisoan"), "");
		JPAQuery<HinhAnh> q = find(HinhAnh.class)
				.where(QHinhAnh.hinhAnh.trangThai.ne(core().TT_DA_XOA));
		if (paramTuKhoa != null && !paramTuKhoa.isEmpty()) {
			String tukhoa = "%" + paramTuKhoa + "%";
			q.where(QHinhAnh.hinhAnh.title.like(tukhoa).or(QHinhAnh.hinhAnh.description.like(tukhoa))
					.or(QHinhAnh.hinhAnh.subTitle.like(tukhoa)).or(QHinhAnh.hinhAnh.content.like(tukhoa)));
		}
		if (getFixTuNgay() != null && getFixDenNgay() == null) {
			q.where(QHinhAnh.hinhAnh.publishBeginTime.after(getFixTuNgay()));
		} else if (getFixTuNgay() == null && getFixDenNgay() != null) {
			q.where(QHinhAnh.hinhAnh.publishBeginTime.before(getFixDenNgay()));
		} else if (getFixTuNgay() != null && getFixDenNgay() != null) {
			q.where(QHinhAnh.hinhAnh.publishBeginTime.between(getFixTuNgay(), getFixDenNgay()));
		}
		if (paramTrangThaiNoiBat.equals("true")) {
			q.where(QHinhAnh.hinhAnh.noiBat.eq(true));
		} else if(paramTrangThaiNoiBat.equals("false")) {
			q.where(QHinhAnh.hinhAnh.noiBat.eq(false));
		}
		if (!paramTrangThaiSoan.isEmpty()) {
			q.where(QHinhAnh.hinhAnh.trangThaiSoan.eq(paramTrangThaiSoan));
		}
		if (tacGia > 0) {
			q.where(QHinhAnh.hinhAnh.author.id.eq(tacGia));
		}
		if (chude > 0) {
			Category cat = em().find(Category.class, chude);
			List<Category> children = find(Category.class).where(QCategory.category.parent.eq(cat))
					.where(QCategory.category.trangThai.eq(core().TT_AP_DUNG)).fetch();
			q.where(QHinhAnh.hinhAnh.categories.any().in(children).or(QHinhAnh.hinhAnh.categories.contains(cat)));
		}
		String paramTrangPublish = MapUtils.getString(argDeco(), Labels.getLabel("param.publishstatus"), "");
		if (argDeco().get(Labels.getLabel("param.publishstatus")) != null && !paramTrangPublish.isEmpty()) {
			boolean trangThaiXuatBan = MapUtils.getBooleanValue(argDeco(), Labels.getLabel("param.publishstatus"));
			q.where(QHinhAnh.hinhAnh.publishStatus.eq(trangThaiXuatBan));
		}
		return q.orderBy(QHinhAnh.hinhAnh.ngayTao.desc());
	}
	
	//Lấy bài viết nổi bật, truyền tham số số lượng bài viết
	public JPAQuery<HinhAnh> getBaiVietNoiBats(int limit) {
		JPAQuery<HinhAnh> q = find(HinhAnh.class)
			.where(QHinhAnh.hinhAnh.trangThai.ne(core().TT_DA_XOA)
					.and(QHinhAnh.hinhAnh.noiBat.eq(true))
					.and(QHinhAnh.hinhAnh.publishStatus.eq(true)
					.and(QHinhAnh.hinhAnh.publishBeginTime.before(getToday()))
					.and(QHinhAnh.hinhAnh.publishEndTime.after(getToday())
							.or(QHinhAnh.hinhAnh.publishEndTime.isNull())
					)));
		if(limit > 0) {
			q.limit(limit);
		}
		return q.orderBy(QHinhAnh.hinhAnh.publishBeginTime.desc());
	}
	
	//Lấy bài viết theo chủ đề
	public JPAQuery<HinhAnh> getBaiVietByCategory(Long catId, int limit) {
		JPAQuery<HinhAnh> q = find(HinhAnh.class)
			.where(QHinhAnh.hinhAnh.trangThai.ne(core().TT_DA_XOA)
					.and(QHinhAnh.hinhAnh.publishStatus.eq(true)
					.and(QHinhAnh.hinhAnh.publishBeginTime.before(getEndToday()))
					.and(QHinhAnh.hinhAnh.publishEndTime.after(getBeginToday())
							.or(QHinhAnh.hinhAnh.publishEndTime.isNull())
					)));
		if(catId >= 0) {
			q.where(QHinhAnh.hinhAnh.categories.any().id.eq(catId));
		}
		if(limit > 0) {
			q.limit(limit);
		}
		return q.orderBy(QHinhAnh.hinhAnh.publishBeginTime.desc());
	}
	
	//Lấy bài viết theo list chủ đề
	public JPAQuery<HinhAnh> getBaiVietByCategory(List<Long> cats, Long catCha, int limit) {
		JPAQuery<HinhAnh> q = find(HinhAnh.class)
			.where(QHinhAnh.hinhAnh.trangThai.ne(core().TT_DA_XOA)
					.and(QHinhAnh.hinhAnh.publishStatus.eq(true))
					.and(QHinhAnh.hinhAnh.publishBeginTime.before(getEndToday()))
					.and(QHinhAnh.hinhAnh.publishEndTime.after(getBeginToday())
							.or(QHinhAnh.hinhAnh.publishEndTime.isNull())
					));
		if(cats != null && catCha != null) {
			q.where((QHinhAnh.hinhAnh.noiBat.eq(true).and(QHinhAnh.hinhAnh.categories.any().id.in(cats)))
					.or(QHinhAnh.hinhAnh.categories.any().id.eq(catCha)));
		}
		if(limit > 0) {
			q.limit(limit);
		}
		return q.orderBy(QHinhAnh.hinhAnh.publishBeginTime.desc());
	}
	
	// Lấy bài viết theo id
	public JPAQuery<HinhAnh> getBaiVietById(Long id) {
		JPAQuery<HinhAnh> q = find(HinhAnh.class)
				.where(QHinhAnh.hinhAnh.trangThai.ne(core().TT_DA_XOA)
				.and(QHinhAnh.hinhAnh.publishStatus.eq(true))
				.and(QHinhAnh.hinhAnh.publishBeginTime.before(getEndToday()))
				.and(QHinhAnh.hinhAnh.publishEndTime.after(getBeginToday())
						.or(QHinhAnh.hinhAnh.publishEndTime.isNull()))
				.and(QHinhAnh.hinhAnh.id.eq(id)));
		return q;
	}
	
	// Lấy bài viết liên quan
	public JPAQuery<HinhAnh> getBaiVietLienQuan(Long chuDe, Long HinhAnh, int limit) {
		JPAQuery<HinhAnh> q = find(HinhAnh.class)
				.where(QHinhAnh.hinhAnh.trangThai.ne(core().TT_DA_XOA)
				.and(QHinhAnh.hinhAnh.publishStatus.eq(true))
				.and(QHinhAnh.hinhAnh.publishBeginTime.before(getEndToday()))
				.and(QHinhAnh.hinhAnh.publishEndTime.after(getBeginToday())
						.or(QHinhAnh.hinhAnh.publishEndTime.isNull()))
				.and(QHinhAnh.hinhAnh.categories.any().id.eq(chuDe))
				.and(QHinhAnh.hinhAnh.id.ne(HinhAnh)));
		return q.orderBy(QHinhAnh.hinhAnh.publishBeginTime.desc()).limit(limit);
	}

	public String getStrUpdate() {
		return strUpdate;
	}

	public void setStrUpdate(String strUpdate) {
		this.strUpdate = strUpdate;
	}

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

	public boolean isUpdateThanhCong() {
		return updateThanhCong;
	}

	public void setUpdateThanhCong(boolean updateThanhCong) {
		this.updateThanhCong = updateThanhCong;
	}

	public boolean isUpdate() {
		return update;
	}

	public void setUpdate(boolean update) {
		this.update = update;
	}
	
}
