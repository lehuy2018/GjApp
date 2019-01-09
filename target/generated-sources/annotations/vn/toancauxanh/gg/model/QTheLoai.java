package vn.toancauxanh.gg.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QTheLoai is a Querydsl query type for TheLoai
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QTheLoai extends EntityPathBase<TheLoai> {

    private static final long serialVersionUID = 2069297819L;

    private static final PathInits INITS = new PathInits("*", "nguoiSua.*.*.*.*", "nguoiTao.*.*.*.*");

    public static final QTheLoai theLoai = new QTheLoai("theLoai");

    public final vn.toancauxanh.model.QModel _super;

    public final NumberPath<Long> clickCount = createNumber("clickCount", Long.class);

    //inherited
    public final BooleanPath daXoa;

    public final StringPath description = createString("description");

    public final StringPath extension = createString("extension");

    //inherited
    public final NumberPath<Long> id;

    public final StringPath name = createString("name");

    public final BooleanPath newTab = createBoolean("newTab");

    public final DateTimePath<java.util.Date> ngayBatDau = createDateTime("ngayBatDau", java.util.Date.class);

    public final DateTimePath<java.util.Date> ngayHetHan = createDateTime("ngayHetHan", java.util.Date.class);

    //inherited
    public final DateTimePath<java.util.Date> ngaySua;

    //inherited
    public final DateTimePath<java.util.Date> ngayTao;

    // inherited
    public final vn.toancauxanh.model.QNhanVien nguoiSua;

    // inherited
    public final vn.toancauxanh.model.QNhanVien nguoiTao;

    public final NumberPath<Integer> soThuTu = createNumber("soThuTu", Integer.class);

    public final StringPath theloaiLink = createString("theloaiLink");

    public final StringPath theloaiUrl = createString("theloaiUrl");

    public final StringPath title = createString("title");

    //inherited
    public final StringPath trangThai;

    public final NumberPath<Long> viewCount = createNumber("viewCount", Long.class);

    public QTheLoai(String variable) {
        this(TheLoai.class, forVariable(variable), INITS);
    }

    public QTheLoai(Path<? extends TheLoai> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QTheLoai(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QTheLoai(PathMetadata metadata, PathInits inits) {
        this(TheLoai.class, metadata, inits);
    }

    public QTheLoai(Class<? extends TheLoai> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this._super = new vn.toancauxanh.model.QModel(type, metadata, inits);
        this.daXoa = _super.daXoa;
        this.id = _super.id;
        this.ngaySua = _super.ngaySua;
        this.ngayTao = _super.ngayTao;
        this.nguoiSua = _super.nguoiSua;
        this.nguoiTao = _super.nguoiTao;
        this.trangThai = _super.trangThai;
    }

}

