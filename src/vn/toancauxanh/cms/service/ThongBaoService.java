package vn.toancauxanh.cms.service;

import com.querydsl.jpa.impl.JPAQuery;
import org.apache.commons.collections.MapUtils;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.util.resource.Labels;
import org.zkoss.zul.DefaultTreeModel;
import org.zkoss.zul.TreeNode;
import vn.toancauxanh.gg.model.Language;
import vn.toancauxanh.gg.model.QThongBao;
import vn.toancauxanh.gg.model.ThongBao;
import vn.toancauxanh.service.BasicService;

import java.util.ArrayList;
import java.util.List;

public class ThongBaoService extends BasicService<ThongBao> {

	public JPAQuery<ThongBao> getTargetQuery() {
		String paramImage = MapUtils.getString(argDeco(),Labels.getLabel("param.tukhoa"), "").trim();
		String trangThai = MapUtils.getString(argDeco(),Labels.getLabel("param.trangthai"),"");
		JPAQuery<ThongBao> q = find(ThongBao.class)
				.where(QThongBao.thongBao.trangThai.ne(core().TT_DA_XOA));
		
		if (paramImage != null && !paramImage.isEmpty()) {
			String tukhoa = "%" + paramImage + "%";
			q.where(QThongBao.thongBao.description.like(tukhoa));
		}
		if (!trangThai.isEmpty()) {
			q.where(QThongBao.thongBao.trangThai.eq(trangThai));
		}
		return q.orderBy(QThongBao.thongBao.ngaySua.desc());
	}

	public List<ThongBao> getLinks() {
		return this.getTargetQuery().fetch();
	}

	public List<ThongBao> getListLink() {
		List<ThongBao> links = new ArrayList<>();
		links.addAll(this.find(ThongBao.class)
				.where(QThongBao.thongBao.trangThai.eq(this.core().TT_AP_DUNG)).fetch());

		return links;
	}
	
	private String img = "/backend/assets/img/edit.png";
	private String hoverImg = "/backend/assets/img/edit_hover.png";
	private String strUpdate = "Thứ tự";
	private boolean update = true;
	private boolean updateThanhCong = true;
	

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

	public List<ThongBao> getCategoryChildren(ThongBao category) {
		List<ThongBao> list = new ArrayList<>();
		if (!category.getTrangThai().equalsIgnoreCase(core().TT_DA_XOA)) {
			for (TreeNode<ThongBao> el : category.getNode().getChildren()) {
				list.add(el.getData());
				list.addAll(getCategoryChildren(el.getData()));
			}
		}
		return list;
	}
	
	public List<ThongBao> getCategoryChildrenButThis(ThongBao category, ThongBao ignore) {
		List<ThongBao> list = new ArrayList<>();
		if (!category.getTrangThai().equalsIgnoreCase(core().TT_DA_XOA)) {
			for (TreeNode<ThongBao> el : category.getNode().getChildren()) {
				if(ignore.getId() != el.getData().getId()) {
					list.add(el.getData());
					list.addAll(getCategoryChildrenButThis(el.getData(), ignore));
				}
			}
		}
		return list;
	}

	public List<ThongBao> getListAllCategoryAndNull(String docType) {
		List<ThongBao> list = new ArrayList<>();
		list.add(null);
		for (ThongBao category : getList()) {
			list.addAll(getCategoryChildren(category));
		}
		return list;
	}
	
	public List<ThongBao> getListAllCategoryAndNullButThis(ThongBao self) {
		List<ThongBao> list = new ArrayList<>();
		list.add(null);
		for (ThongBao category : getListButThis(self)) {
			if(self.getId() != category.getId()) {
				list.add(category);
				list.addAll(getCategoryChildrenButThis(category,self));
			}
		}
		return list;
	}
	public List<ThongBao> getListButThis(ThongBao self) {
		JPAQuery<ThongBao> q = find(ThongBao.class);
		q.where(QThongBao.thongBao.trangThai.ne(core().TT_DA_XOA))
				.where(QThongBao.thongBao.parent.isNull());
		q.orderBy(QThongBao.thongBao.soThuTu.asc());
		if(self != null && !self.noId()) {
			q.where(QThongBao.thongBao.id.ne(self.getId()));
		}
		List<ThongBao> list = q.fetch();
		for (ThongBao category : list) {
			category.loadChildren();
		}
		return list;
	}
	
	public List<ThongBao> getListAllCategoryDocsAndNull() {
		List<ThongBao> list = new ArrayList<>();
		list.add(null);
		for (ThongBao category : getList()) {
			if (Labels.getLabel("type.vanban").equalsIgnoreCase(
					category.getDocumentType())
					|| Labels.getLabel("type.tulieu").equalsIgnoreCase(
							category.getDocumentType())
					|| Labels.getLabel("type.vankien").equalsIgnoreCase(
							category.getDocumentType())) {
				list.addAll(getCategoryChildren(category));
			}
		}
		return list;
	}
	
	public List<ThongBao> getListVanKienTuLieuAndNull() {
		List<ThongBao> list = new ArrayList<>();
		list.add(null);
		list.addAll(getListVanKienTuLieu());
		return list;
	}
	
	public List<ThongBao> getListVanKienTuLieu() {
		List<ThongBao> list = new ArrayList<>();
		for (ThongBao category : getList()) {
			if (Labels.getLabel("type.vanban").equalsIgnoreCase(category.getDocumentType())) {
				list.addAll(getCategoryChildren(category));
			}
		}
		List<ThongBao> clone = new ArrayList<>(list);
		for (ThongBao c : list) {
			if (c.getAlias().equals("vanbanmoi") || c.getParent().getAlias().equals("vanbanmoi")) {
				clone.remove(c);
			}
		}
		return clone;
	}

	public List<ThongBao> getListAllCategory(String docType) {
		List<ThongBao> list = new ArrayList<>();
		for (ThongBao category : getList()) {
			list.addAll(getCategoryChildren(category));
		}
		return list;
	}

	public List<ThongBao> getListAllCategory() {
		List<ThongBao> list = new ArrayList<>();
		for (ThongBao category : getList()) {
			list.add(category);
			list.addAll(getCategoryChildren(category));
		}
		return list;
	}
	
	public List<ThongBao> getListAllCategorySearch(String code) {
		List<ThongBao> list = new ArrayList<>();
		list.add(null);
		for (ThongBao category : getList()) {
			if (category.getLanguage().getCode().equals(code)) {
				list.addAll(getCategoryChildren(category));
			}
		}
		return list;
	}
	
	public List<ThongBao> getListAllCategoryAndNull() {
		List<ThongBao> list = new ArrayList<>();
		list.add(null);
		for (ThongBao category : getList()) {
			list.add(category);
			list.addAll(getCategoryChildren(category));
		}
		return list;
	}
	
	public List<ThongBao> getListThuTucHanhChinhAndNull() {
		List<ThongBao> list = new ArrayList<>();
		list.add(null);
		list.addAll(find(ThongBao.class)
				.where(QThongBao.thongBao.trangThai.eq(core().TT_AP_DUNG))
				.where(QThongBao.thongBao.parent.isNotNull())
				.fetch());
		return list;
	}
	
	public List<ThongBao> getListThuTucHanhChinh() {
		List<ThongBao> list = new ArrayList<>();
		list.addAll(find(ThongBao.class)
				.where(QThongBao.thongBao.trangThai.eq(core().TT_AP_DUNG))
				.where(QThongBao.thongBao.parent.isNotNull())
				.fetch());
		return list;
	}
	
	public List<ThongBao> getList() {
		JPAQuery<ThongBao> q = find(ThongBao.class);
		q.where(QThongBao.thongBao.trangThai.ne(core().TT_DA_XOA))
				.where(QThongBao.thongBao.parent.isNull());
		q.orderBy(QThongBao.thongBao.soThuTu.asc());
		List<ThongBao> list = q.fetch();
		for (ThongBao category : list) {
			category.loadChildren();
		}
		return list;
	}
	
	public List<ThongBao> getListDanhMucCungCapRss() {
		List<ThongBao> list = new ArrayList<>();
		ThongBao catTtsk = em().find(ThongBao.class, Long.valueOf(Labels.getLabel("conf.cid.baiviet.vn")));
		catTtsk.loadChildrenRss();
		list.addAll(getCategoryChildrenRss(catTtsk));
		return list;
	}
	
	public List<ThongBao> getListDanhMucCungCapRss(String langCode) {
		List<ThongBao> list = new ArrayList<>();
		ThongBao catTtsk = new ThongBao();
		if(langCode.equals("vn")){
			catTtsk = em().find(ThongBao.class, Long.valueOf(Labels.getLabel("conf.cid.baiviet.vn")));
		} else {
			catTtsk = em().find(ThongBao.class, Long.valueOf(Labels.getLabel("conf.cid.baiviet.en")));
		}
			catTtsk.loadChildrenRss();
		list.addAll(getCategoryChildrenRss(catTtsk));
		return list;
	}
	
	public List<ThongBao> getCategoryChildrenRss(ThongBao category) {
		List<ThongBao> list = new ArrayList<>();
		if (!category.getTrangThai().equalsIgnoreCase(core().TT_DA_XOA) && category.isCungCapRss()) {
			for (TreeNode<ThongBao> el : category.getNode().getChildren()) {
				list.add(el.getData());
				list.addAll(getCategoryChildrenRss(el.getData()));
			}
		}
		return list;
	}
	
	public Language getArgLang() {
		long id = MapUtils.getLongValue(argDeco(), "language");
		Language lang = em().find(Language.class, id);
		return lang;
	}
	
	public List<ThongBao> getList2() {
		String param = MapUtils.getString(argDeco(),"tukhoa","").trim();
		String trangThai = MapUtils.getString(argDeco(),"trangthai","");
		JPAQuery<ThongBao> q = find(ThongBao.class);
		q.where(QThongBao.thongBao.trangThai.ne(core().TT_DA_XOA))
				.where(QThongBao.thongBao.parent.isNull());
		if (!trangThai.isEmpty()) {
			q.where(QThongBao.thongBao.trangThai.eq(trangThai));
		}
		q.orderBy(QThongBao.thongBao.soThuTu.asc());
		List<ThongBao> list = q.fetch();
		for (ThongBao category : list) {
			if(category.getName().toLowerCase().contains(param.toLowerCase()) && (trangThai.isEmpty() || (!trangThai.isEmpty() && category.getTrangThai().equals(trangThai)))){
				category.loadChildren();
			} 
			else{
				category.loadChildren(param, trangThai);
			}
		}
		return list;
	}

	public DefaultTreeModel<ThongBao> getModel() {
		String param = MapUtils.getString(argDeco(),"tukhoa","").trim();
		String trangThai = MapUtils.getString(argDeco(),"trangthai","");
		ThongBao catGoc = new ThongBao();
		DefaultTreeModel<ThongBao> model = new DefaultTreeModel<>(catGoc.getNode(), true);
		for (ThongBao cat : getList2()) {
			if ((cat.getName().toLowerCase().contains(param.toLowerCase()) && cat.getTrangThai().contains(trangThai))|| cat.loadSizeChild() > 0) {
				catGoc.getNode().add(cat.getNode());
			}
		}
		if (!param.isEmpty() || !param.equals("") || !trangThai.isEmpty() || !trangThai.equals("")) {
			catGoc.getNode().getModel().setOpenObjects(catGoc.getNode().getChildren());
		}
		//model.setOpenObjects(catGoc.getNode().getChildren());
		openObject(model, catGoc.getNode());
		BindUtils.postNotifyChange(null, null, this, "sizeOfCategories");
		return model;
	}
	
	public void openObject(DefaultTreeModel<ThongBao> model, TreeNode<ThongBao> node){
		if(node.isLeaf()){
			model.addOpenObject(node);
		} else {
			for (TreeNode<ThongBao> child : node.getChildren()) {
				model.addOpenObject(child);
				openObject(node.getModel(), child);
			}
		}
	}
	
	public void fixSoThuTu() {
		int i = 1;
		for (ThongBao category : getList()) {
			category.setSoThuTu(i);
			category.save();
			int j = 1;
			for (ThongBao cat : getCategoryChildren(category)) {
				if(cat.getParent().equals(category)){
					cat.setSoThuTu(j);
					cat.save();
					int idx = 1;
					for (ThongBao c : getCategoryChildren(cat)) {
						if(c.getParent().equals(cat)){
							c.setSoThuTu(idx);
							c.save();
							int k = 1;
							for (ThongBao a : getCategoryChildren(c)) {
								if(a.getParent().equals(c)){
									a.setSoThuTu(k);
									a.save();
									k++;
								}		
							}
							idx++;
						}		
					}
					j++;
				}		
			}
			i ++;
		}
	}

	@Command
	public void clickButton(@BindingParam("model") final List<ThongBao> model) {
		if (strUpdate.equals("Thứ tự")) {
			setStrUpdate("Lưu thứ tự");
			setImg("/backend/assets/img/save.png");
			setHoverImg("/backend/assets/img/save_hover.png");
			setUpdate(false);
		} else {
			setUpdateThanhCong(true);
			
			if (model == null) {
				for (ThongBao cat: listChuDeThayDoiThuTu) {
					if (check(cat)) {
						setUpdateThanhCong(false);
						break;
					}
					cat.save();
				}
			} else {
				for (ThongBao cat: model) {
					if (check(cat)) {
						setUpdateThanhCong(false);
						break;
					}
					cat.save();
				}
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
		BindUtils.postNotifyChange(null, null, this, "list");
		BindUtils.postNotifyChange(null, null, this, "model");
		BindUtils.postNotifyChange(null, null, this, "targetQueryTheLoai");
	}
	
	private boolean check (ThongBao cat) {
		if ("".equals(cat.getSoThuTu()) || cat.getSoThuTu() <= 0) {
			return true;
		}
		return false;
	}
	private List<ThongBao> listChuDeThayDoiThuTu = new ArrayList<>();
	
	
	public List<ThongBao> getListChuDeThayDoiThuTu() {
		return listChuDeThayDoiThuTu;
	}

	public void setListChuDeThayDoiThuTu(List<ThongBao> listChuDeThayDoiThuTu) {
		this.listChuDeThayDoiThuTu = listChuDeThayDoiThuTu;
	}

	public void addListChuDeThayDoiThuTu(ThongBao category, int stt) {
		if (listChuDeThayDoiThuTu.contains(category)) {
			listChuDeThayDoiThuTu.remove(category);
			category.setSoThuTu(stt);
			listChuDeThayDoiThuTu.add(category);
		} else {
			category.setSoThuTu(stt);
			listChuDeThayDoiThuTu.add(category);
		}
	}
	
	public JPAQuery<ThongBao> getTargetQueryTheLoai() {
		String paramImage = MapUtils.getString(argDeco(),Labels.getLabel("param.tukhoa"),"").trim();
		String trangThai = MapUtils.getString(argDeco(),Labels.getLabel("param.trangthai"),"");

		JPAQuery<ThongBao> q = find(ThongBao.class)
				.where(QThongBao.thongBao.trangThai.ne(core().TT_DA_XOA));

		if (paramImage != null && !paramImage.isEmpty()) {
			String tukhoa = "%" + paramImage + "%";
			q.where(QThongBao.thongBao.name.like(tukhoa));
		}
		if (!trangThai.isEmpty()) {
			q.where(QThongBao.thongBao.trangThai.eq(trangThai));
		}
		return q.orderBy(QThongBao.thongBao.soThuTu.asc());
	}
	
	public List<ThongBao> getListChuDeVanBanChiDao() {
		List<ThongBao> list = new ArrayList<>();
		list.addAll(find(ThongBao.class)
				.where(QThongBao.thongBao.trangThai.eq(core().TT_AP_DUNG))
				.where(QThongBao.thongBao.parent.id.eq(Long.valueOf(Labels.getLabel("conf.cid.vanbanchidao.".concat(this.getArgLang().getCode())))))
				.fetch());
		return list;
	}
	
	public List<ThongBao> getListChuDeVanBanChiDaoAndNull() {
		List<ThongBao> list = new ArrayList<>();
		list.add(null);
		list.addAll(getListChuDeVanBanChiDao());
		return list;
	}
	
	public List<ThongBao> getListLinhVucVanBanDuThao() {
		List<ThongBao> list = new ArrayList<>();
		list.addAll(find(ThongBao.class)
				.where(QThongBao.thongBao.trangThai.eq(core().TT_AP_DUNG))
				.where(QThongBao.thongBao.parent.id.eq(Long.valueOf(Labels.getLabel("conf.cid.vanbanduthao.".concat(this.getArgLang().getCode())))))
				.fetch());
		return list;
	}
	
	public List<ThongBao> getListLinhVucVanBanDuThaoAndNull() {
		List<ThongBao> list = new ArrayList<>();
		list.add(null);
		list.addAll(getListLinhVucVanBanDuThao());
		return list;
	}
	
	public List<ThongBao> getListLinhVucDeTaiKhoaHoc() {
		List<ThongBao> list = new ArrayList<>();
		long idLinhVuc = Long.valueOf(Labels.getLabel("conf.cid.linhvucdetaikhoahoc"));
		list.addAll(find(ThongBao.class)
				.where(QThongBao.thongBao.trangThai.eq(core().TT_AP_DUNG))
				.where(QThongBao.thongBao.parent.id.eq(idLinhVuc))
				.fetch());
		return list;
	}
	public List<ThongBao> getListLinhVucDeTaiKhoaHocAndNull() {
		List<ThongBao> list = new ArrayList<>();
		list.add(null);
		list.addAll(getListLinhVucDeTaiKhoaHoc());
		return list;
	}
	
	public long getSizeOfCategories() {
		String param = MapUtils.getString(argDeco(),"tukhoa","").trim();
		String trangThai = MapUtils.getString(argDeco(),"trangthai","");
		JPAQuery<ThongBao> q = find(ThongBao.class)
				.where(QThongBao.thongBao.trangThai.ne(core().TT_DA_XOA));
		if(!trangThai.isEmpty()) {
			q.where(QThongBao.thongBao.trangThai.eq(trangThai));
		}
		if(!param.isEmpty()) {
			q.where(QThongBao.thongBao.name.toLowerCase().contains(param.toLowerCase()));
		}
		return q.fetchCount();
	}
	
	public JPAQuery<ThongBao> getthongBaoByListId(List<Long> ids) {
		if(ids != null && !ids.isEmpty()) {
			JPAQuery<ThongBao> q = find(ThongBao.class)
					.where(QThongBao.thongBao.trangThai.ne(core().TT_DA_XOA)
							.and(QThongBao.thongBao.id.in(ids)).and(QThongBao.thongBao.trangThai.eq(core().TT_AP_DUNG)));
			return q.orderBy(QThongBao.thongBao.soThuTu.asc());
		}
		
		return find(ThongBao.class).where(QThongBao.thongBao.id.eq(0l));
	}
	
	public JPAQuery<ThongBao> getChild(Long chaId) {
		if(chaId != null && chaId > 0) {
			JPAQuery<ThongBao> q = find(ThongBao.class)
					.where(QThongBao.thongBao.trangThai.ne(core().TT_DA_XOA)
							.and(QThongBao.thongBao.parent.id.eq(chaId))
							.and(QThongBao.thongBao.trangThai.eq(core().TT_AP_DUNG)));
			return q.orderBy(QThongBao.thongBao.soThuTu.asc());
		}
		return find(ThongBao.class).where(QThongBao.thongBao.id.eq(0l));
	}
	
	public JPAQuery<ThongBao> getById(Long id) {
		if(id != null && id > 0) {
			JPAQuery<ThongBao> q = find(ThongBao.class)
					.where(QThongBao.thongBao.trangThai.ne(core().TT_DA_XOA)
							.and(QThongBao.thongBao.id.eq(id))
							.and(QThongBao.thongBao.trangThai.eq(core().TT_AP_DUNG)));
			return q.orderBy(QThongBao.thongBao.soThuTu.asc());
		}
		return find(ThongBao.class).where(QThongBao.thongBao.id.eq(0l));
	}
	
 }
