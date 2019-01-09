package vn.toancauxanh.gg.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QThongBao is a Querydsl query type for ThongBao
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QThongBao extends EntityPathBase<ThongBao> {

    private static final long serialVersionUID = 41145917L;

    private static final PathInits INITS = new PathInits("*", "nguoiSua.*.*.*.*", "nguoiTao.*.*.*.*");

    public static final QThongBao thongBao = new QThongBao("thongBao");

    public final vn.toancauxanh.model.QModel _super;

    public final StringPath alias = createString("alias");

    public final BooleanPath cungCapRss = createBoolean("cungCapRss");

    //inherited
    public final BooleanPath daXoa;

    public final StringPath description = createString("description");

    //inherited
    public final NumberPath<Long> id;

    public final QLanguage language;

    public final StringPath name = createString("name");

    //inherited
    public final DateTimePath<java.util.Date> ngaySua;

    //inherited
    public final DateTimePath<java.util.Date> ngayTao;

    // inherited
    public final vn.toancauxanh.model.QNhanVien nguoiSua;

    // inherited
    public final vn.toancauxanh.model.QNhanVien nguoiTao;

    public final StringPath note = createString("note");

    public final QThongBao parent;

    public final NumberPath<Integer> soThuTu = createNumber("soThuTu", Integer.class);

    //inherited
    public final StringPath trangThai;

    public final BooleanPath visible = createBoolean("visible");

    public QThongBao(String variable) {
        this(ThongBao.class, forVariable(variable), INITS);
    }

    public QThongBao(Path<? extends ThongBao> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QThongBao(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QThongBao(PathMetadata metadata, PathInits inits) {
        this(ThongBao.class, metadata, inits);
    }

    public QThongBao(Class<? extends ThongBao> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this._super = new vn.toancauxanh.model.QModel(type, metadata, inits);
        this.daXoa = _super.daXoa;
        this.id = _super.id;
        this.language = inits.isInitialized("language") ? new QLanguage(forProperty("language"), inits.get("language")) : null;
        this.ngaySua = _super.ngaySua;
        this.ngayTao = _super.ngayTao;
        this.nguoiSua = _super.nguoiSua;
        this.nguoiTao = _super.nguoiTao;
        this.parent = inits.isInitialized("parent") ? new QThongBao(forProperty("parent"), inits.get("parent")) : null;
        this.trangThai = _super.trangThai;
    }

}

