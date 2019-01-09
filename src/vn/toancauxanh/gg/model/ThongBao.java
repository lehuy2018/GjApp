package vn.toancauxanh.gg.model;

import com.google.common.base.Strings;
import com.querydsl.jpa.impl.JPAQuery;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.validator.AbstractValidator;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.DefaultTreeNode;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.TreeNode;
import org.zkoss.zul.Window;
import vn.toancauxanh.model.Model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The persistent class for the category database table.
 * 
 */
@Entity
@Table(name = "thongbao", indexes = {
		@Index(columnList = "name"), 
		@Index(columnList = "alias"),
		@Index(columnList = "soThuTu")})
public class ThongBao extends Model<ThongBao> {
	public static transient final Logger LOG = LogManager.getLogger(ThongBao.class.getName());

	private String name = "";
	private String description = "";
	private ThongBao parent;
	private String alias = "";
	private transient int level;
	private boolean visible;
	private int soThuTu;
	private boolean cungCapRss;
	private Language language;
	private String note = "";

	public ThongBao() {
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	@ManyToOne
	public Language getLanguage() {
		return language;
	}
	public void setLanguage(Language language) {
		this.language = language;
	}

	@Column(length = 255)
	public String getName() {
		return this.name;
	}
	public void setName( String name1) {
		this.name = Strings.nullToEmpty(name1);
	}

	@Column(name = "visible", columnDefinition = "boolean default true")
	public boolean isVisible() {
		return visible;
	}
	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	@Column(name = "cungcaprss", columnDefinition = "boolean default true")
	public boolean isCungCapRss() {
		return cungCapRss;
	}
	public void setCungCapRss(boolean cungCapRss) {
		this.cungCapRss = cungCapRss;
	}

	@ManyToOne
	@JoinColumn(name = "parent_id")
	public ThongBao getParent() {
		return this.parent;
	}
	public void setParent(ThongBao category1) {
		this.parent = category1;
	}

//	@Lob
	public String getDescription() {
		return description;
	}
	public void setDescription( String description1) {
		this.description = Strings.nullToEmpty(description1);
	}

	public String getAlias() {
		if (alias.isEmpty() && "".equals(alias)) {
			alias = unAccent(getName());
		}
		return alias;
	}
	public void setAlias( String alias1) {
		this.alias = Strings.nullToEmpty(alias1);
	}

	public int getSoThuTu() {
		return soThuTu;
	}
	public void setSoThuTu(int _soThuTu) {
		this.soThuTu = _soThuTu;
	}

	// Transient
	private transient final TreeNode<ThongBao> node = new DefaultTreeNode<>(
			this, new ArrayList<DefaultTreeNode<ThongBao>>());
	@Transient
	public TreeNode<ThongBao> getNode() {
		return node;
	}

	@Transient
	public String getChildName() {
		int count = 0;
		String s = " ";
		for (ThongBao cha = getParent(); cha != null; cha = cha.getParent())
			count = count + 1;
		for (int i = 0; i < count; i++)
			s += " - ";
		return s + this.name;
	}

	public void loadChildrenRss() {
		for (final ThongBao con : find(ThongBao.class)
				.where(QThongBao.thongBao.parent.eq(this))
				.where(QThongBao.thongBao.trangThai.ne(core().TT_DA_XOA))
				.where(QThongBao.thongBao.cungCapRss.eq(true))
				.orderBy(QThongBao.thongBao.soThuTu.asc())
				.fetch()) {
			if (!node.getChildren().contains(con.getNode())) {
				con.loadChildrenRss();
				node.add(con.getNode());
			}
		}
	}

	public void loadChildren() {
		for (final ThongBao con : find(ThongBao.class)
				.where(QThongBao.thongBao.parent.eq(this))
				.where(QThongBao.thongBao.trangThai.ne(core().TT_DA_XOA))
				.orderBy(QThongBao.thongBao.soThuTu.asc())
				.fetch()) {
			con.loadChildren();
			node.add(con.getNode());
		}
	}

	public void loadChildren(String param, String trangThai) {
		for (final ThongBao con : find(ThongBao.class)
				.where(QThongBao.thongBao.parent.eq(this))
				.where(QThongBao.thongBao.trangThai.ne(core().TT_DA_XOA))
				.orderBy(QThongBao.thongBao.soThuTu.asc())
				.fetch()) {
			if(con.getName().toLowerCase().contains(param.toLowerCase()) && (trangThai.isEmpty() || (!trangThai.isEmpty() && con.getTrangThai().equals(trangThai)))){
				con.loadChildren();
				node.add(con.getNode());
			} else{
				con.loadChildren(param, trangThai);
				if (con.loadSizeChild() > 0) {
					node.add(con.getNode());
				}
			}
		}
		//new DefaultTreeModel<Category>(node, true);
		//node.getModel().setOpenObjects(node.getChildren());
	}

	public int loadSizeChild() {
		int size = core().getThongBaos().getCategoryChildren(this).size();
		return size;
	}
	@Command
	public void saveChuDeMain(@BindingParam("list") final Object listObject,
			@BindingParam("wdn") final Window wdn) {
		setName(getName().trim().replaceAll("\\s+", " "));
		save();
		wdn.detach();
		BindUtils.postNotifyChange(null, null, listObject, "*");
	}

	@Command
	public void saveChude(@BindingParam("node") DefaultTreeNode<ThongBao> node1,
			@BindingParam("tree") org.zkoss.zul.DefaultTreeModel<ThongBao> tree,
			@BindingParam("isAdd") boolean isAdd) {
		LOG.info("save chu de");
		List<ThongBao> list = find(ThongBao.class)
				.where(QThongBao.thongBao.parent.eq(getParent()))
				.where(QThongBao.thongBao.trangThai.ne(core().TT_DA_XOA))
				.fetch();
		if (isAdd) {
			setSoThuTu(list.size()+1);
			node1.add(getNode());
		}
		setName(getName().trim().replaceAll("\\s+", " "));
		save();
		tree.addOpenObject(node1);
		BindUtils.postNotifyChange(null, null, node1, "*");
	}

	@Command
	public void redirectCatagory(
			@BindingParam("zul") String zul,
			@BindingParam("vmArgs") Object vmArgs,
			@BindingParam("node") DefaultTreeNode<ThongBao> node1,
			@BindingParam("tree") org.zkoss.zul.DefaultTreeModel<ThongBao> tree,
			@BindingParam("catSelected") ThongBao catSelected) {
		Map<String, Object> args = new HashMap<>();
		args.put("node", node1);
		args.put("tree", tree);
		args.put("vmArgs", vmArgs);
		args.put("catSelected", catSelected);
		Executions.createComponents(zul, null, args);
	}

	@Transient
	public int getLevel() {
		level = 0;
		checkLevel(this);
		return level;
	}

	public void checkLevel(final ThongBao item) {
		ThongBao parent2 = item.getParent();
		if (parent2 != null) {
			level++;
			checkLevel(parent2);
		}
	}
	@Command
	public void deleteChuDe(
			final @BindingParam("node") DefaultTreeNode<ThongBao> node1,
			final @BindingParam("tree") org.zkoss.zul.DefaultTreeModel<ThongBao> tree,
			final @BindingParam("catSelected") ThongBao catSelected) {
		if (!catSelected.noId() && catSelected.inUse()) {
			showNotification("Không thể xoá thông báo đang được sử dụng",
					"", "error");
			return;
		}

		final List<ThongBao> checkList = core().getThongBaos()
				.getCategoryChildren(catSelected);

		for (ThongBao category : checkList) {
			if (!category.noId() && category.inUse()) {
				showNotification("Không thể xoá thông báo có thông báo con đang được sử dụng",
						"", "error");
				return;
			}
		}

		Messagebox.show("Bạn muốn xóa thông báo này?", "Xác nhận", Messagebox.CANCEL
				| Messagebox.OK, Messagebox.QUESTION,
				new EventListener<Event>() {
					@Override
					public void onEvent(final Event event) {
						if (Messagebox.ON_OK.equals(event.getName())) {
							for (ThongBao category : checkList) {
								category.setTrangThai(core().TT_DA_XOA);
								category.saveNotShowNotification();
							}
							catSelected.setTrangThai(core().TT_DA_XOA);
							catSelected.saveNotShowNotification();
							// ------------
							DefaultTreeNode<ThongBao> nodeParent = (DefaultTreeNode<ThongBao>) node1
									.getParent();
							nodeParent.remove(node1);

							tree.addOpenObject(nodeParent);
							BindUtils.postNotifyChange(null, null, nodeParent,
									"*");
							BindUtils.postNotifyChange(null, null, node1, "*");

							showNotification("Đã xóa thành công!","", "success");
						}
					}
				});
	}
	

	@Command
	public void updatehienThi(@BindingParam("obj") final ThongBao cat) {
		cat.save();
		BindUtils.postNotifyChange(null, null, this, "visible");
	}
	
	@Transient
	public int getSoThuTuByParentId(ThongBao cat) {
		Long dem = find(ThongBao.class)
				.where(QThongBao.thongBao.trangThai.ne(core().TT_DA_XOA))
				.where(QThongBao.thongBao.parent.eq(cat))
				.fetchCount();
		int dem2 = new BigDecimal(dem).intValueExact();
		return dem2 + 1;
	}
	
	@Transient
	public List<ThongBao> getChild() {
		List<ThongBao> list = new ArrayList<ThongBao>();
		list.addAll(core().getThongBaos().getChild(this.getId()).fetch());
		return list;
	}
	
	@Transient
	public List<Long> getChildIds() {
		List<Long> list = new ArrayList<Long>();
		for(ThongBao c : getChild()) {
			list.add(c.getId());
		}
		return list;
	}
	@Transient
	public List<Long> getAllChildIds() {
		List<Long> list = new ArrayList<Long>();
		list.addAll(getChildsOfCat(this));
		return list;
	}
	
	@Transient
	public List<Long> getChildsOfCat(ThongBao cat) {
		List<Long> list = new ArrayList<Long>();
		list.add(cat.getId());
		for(ThongBao c : cat.getChild()) {
			list.addAll(getChildsOfCat(c));
		}
		return list;
	}
	@Transient
	public List<Long> getSelfAndChildIds() {
		List<Long> list = new ArrayList<Long>();
		list.add(this.getId());
		for(ThongBao c : getChild()) {
			list.add(c.getId());
		}
		return list;
	}
	
	@Transient
	public AbstractValidator getValidatorCatChil() {
		return new AbstractValidator() {
			@Override
			public void validate(final ValidationContext ctx) {
				String value = (String)ctx.getProperty().getValue();
				if(value == null || "".equals(value)) {
					addInvalidMessage(ctx, "error","Không được để trống trường này");
				} else {
					JPAQuery<ThongBao> q = find(ThongBao.class)
							.where(QThongBao.thongBao.name.eq(value))
							.where(QThongBao.thongBao.trangThai.ne(core().TT_DA_XOA));
					if(getParent() == null) {
						q.where(QThongBao.thongBao.parent.isNull());
					} else {
						q.where(QThongBao.thongBao.parent.eq(getParent()));
					}
					if(!ThongBao.this.noId()) {
						q.where(QThongBao.thongBao.id.ne(getId()));
					}
					if(q.fetchCount() > 0) {
						addInvalidMessage(ctx, "error","Tên chủ đề đã được sử dụng");
					}
				}
			}
		};
	}
}