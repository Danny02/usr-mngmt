package dev.nullzwo.user.app.adapter;

import dev.nullzwo.user.domain.model.User;
import dev.nullzwo.user.domain.model.UserChanges;

import javax.persistence.*;

@Entity
@NamedNativeQuery(
        name = "count_changes_by_user",
        query = "select userid as userId, min(pseudonym) as pseudonym, count(1) as changeCount from changes group by userid",
        resultSetMapping = "user_changes_dto"
)
@SqlResultSetMapping(
        name = "user_changes_dto",
        classes = @ConstructorResult(
                targetClass = UserChangesDto.class,
                columns = {
                        @ColumnResult(name = "userId", type = String.class),
                        @ColumnResult(name = "pseudonym", type = String.class),
                        @ColumnResult(name = "changeCount", type = Integer.class),
                }
        )
)
public class UserChangesDto {
    @Id
    private String userId;
    private String pseudonym;
    private int changeCount;

    public UserChangesDto() {
    }

    public UserChangesDto(String userId, String pseudonym, int changeCount) {
        this.userId = userId;
        this.pseudonym = pseudonym;
        this.changeCount = changeCount;
    }

    public UserChanges toUserChanges() {
        return new UserChanges(new User.Id(userId), pseudonym, changeCount);
    }
}
