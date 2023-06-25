package ukma.edu.ua.model.command;

public record AddGroupMemberCommand(long productId, long groupId) implements Command {
}
