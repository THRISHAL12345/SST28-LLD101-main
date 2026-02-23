import java.util.*;

public class OnboardingService {

    private final StudentRepository repository;
    private final StudentInputParser parser;
    private final StudentValidator validator;
    private final OnboardingPrinter printer;

    public OnboardingService(StudentRepository repository) {
        this.repository = repository;
        this.parser = new StudentInputParser();
        this.validator = new StudentValidator();
        this.printer = new OnboardingPrinter();
    }

    public void registerFromRawInput(String raw) {

        printer.printInput(raw);

        Map<String,String> kv = parser.parse(raw);

        List<String> errors = validator.validate(kv);

        if (!errors.isEmpty()) {
            printer.printErrors(errors);
            return;
        }

        String id = IdUtil.nextStudentId(repository.count());

        StudentRecord rec = new StudentRecord(
                id,
                kv.get("name"),
                kv.get("email"),
                kv.get("phone"),
                kv.get("program")
        );

        repository.save(rec);

        printer.printSuccess(rec, repository.count());
    }
}