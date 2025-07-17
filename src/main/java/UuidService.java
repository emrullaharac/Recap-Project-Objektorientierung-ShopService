import java.util.UUID;

public class UuidService implements IdService{
    @Override
    public String generateId() {
        return UUID.randomUUID().toString();
    }
}
