package com.heygongc.user.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QUser is a Querydsl query type for User
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUser extends EntityPathBase<User> {

    private static final long serialVersionUID = -530157139L;

    public static final QUser user = new QUser("user");

    public final com.heygongc.global.config.QBaseTimeEntity _super = new com.heygongc.global.config.QBaseTimeEntity(this);

    public final BooleanPath ads = createBoolean("ads");

    public final BooleanPath alarm = createBoolean("alarm");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> created_at = _super.created_at;

    public final DateTimePath<java.time.LocalDateTime> deletedAt = createDateTime("deletedAt", java.time.LocalDateTime.class);

    public final StringPath deviceId = createString("deviceId");

    public final StringPath deviceOs = createString("deviceOs");

    public final StringPath email = createString("email");

    public final StringPath fcmToken = createString("fcmToken");

    public final StringPath id = createString("id");

    public final NumberPath<Long> seq = createNumber("seq", Long.class);

    public final StringPath snsId = createString("snsId");

    public final EnumPath<com.heygongc.user.application.UserSnsType> snsType = createEnum("snsType", com.heygongc.user.application.UserSnsType.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updated_at = _super.updated_at;

    public QUser(String variable) {
        super(User.class, forVariable(variable));
    }

    public QUser(Path<? extends User> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUser(PathMetadata metadata) {
        super(User.class, metadata);
    }

}

