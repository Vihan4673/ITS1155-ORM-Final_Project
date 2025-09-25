package lk.ijse.bo.custom.impl;

import lk.ijse.bo.custom.PaymentBO;
import lk.ijse.dao.DAOFactory;
import lk.ijse.dao.custom.CourseDAO;
import lk.ijse.dao.custom.PaymentDAO;
import lk.ijse.dao.custom.StudentDAO;
import lk.ijse.dto.PaymentDTO;
import lk.ijse.dto.StudentDTO;
import lk.ijse.dto.CourseDTO;
import lk.ijse.entity.Payment;
import lk.ijse.entity.Student;
import lk.ijse.entity.Course;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class PaymentBOImpl implements PaymentBO {

    private final PaymentDAO paymentDAO = (PaymentDAO) DAOFactory.getDAO(DAOFactory.DAOType.PAYMENT);
    private final StudentDAO studentDAO = (StudentDAO) DAOFactory.getDAO(DAOFactory.DAOType.STUDENT);
    private final CourseDAO courseDAO = (CourseDAO) DAOFactory.getDAO(DAOFactory.DAOType.PROGRAM);

    @Override
    public boolean savePayment(PaymentDTO dto) {
        try {
            // Student object only with ID
            Student student = new Student();
            student.setStudentId(dto.getStudentId());

            // Courses list
            List<Course> courses = dto.getProgramIds().stream()
                    .map(id -> {
                        Course c = new Course();
                        c.setProgramId(id);
                        return c;
                    }).collect(Collectors.toList());

            Payment payment = Payment.builder()
                    .paymentId(dto.getPaymentId())
                    .student(student)
                    .programs(courses)  // must be non-null
                    .amount(dto.getAdvanceAmount())
                    .totalFee(dto.getTotalFee())
                    .paymentDate(dto.getPaymentDate())
                    .status(dto.getStatus())
                    .build();

            return paymentDAO.save(payment);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updatePayment(PaymentDTO dto) {
        return paymentDAO.update(toEntity(dto));
    }

    @Override
    public boolean deletePayment(String id) {
        return paymentDAO.delete(id);
    }

    @Override
    public PaymentDTO getPayment(String id) {
        Payment payment = paymentDAO.findById(id);
        return payment != null ? toDTO(payment) : null;
    }

    @Override
    public List<PaymentDTO> getAllPayments() {
        List<Payment> payments = paymentDAO.findAll();
        if (payments == null) payments = List.of(); // null-safe
        return payments.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public String generatePaymentId() {
        return paymentDAO.generatePaymentId();
    }

    @Override
    public List<StudentDTO> getAllStudents() {
        List<Student> students = studentDAO.findAll();
        if (students == null) students = List.of();

        return students.stream()
                .map(s -> {
                    StudentDTO dto = new StudentDTO(
                            s.getStudentId(),
                            s.getName(),
                            s.getAddress(),
                            s.getTel(),
                            s.getEmail() != null ? s.getEmail() : "",
                            s.getRegistrationDate()
                    );

                    if (s.getCourses() != null && !s.getCourses().isEmpty()) {
                        List<String> courseIds = s.getCourses().stream()
                                .map(Course::getProgramId)
                                .collect(Collectors.toList());
                        dto.setEnrolledCourseIds(courseIds);
                    }
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<CourseDTO> getAllPrograms() {
        List<Course> courses = courseDAO.findAll();
        if (courses == null) courses = List.of();

        return courses.stream()
                .map(c -> new CourseDTO(
                        c.getProgramId(),
                        c.getProgramName(),
                        c.getDuration(),
                        c.getFee(),
                        "Months" // Hardcoded
                ))
                .collect(Collectors.toList());
    }

    // DTO -> Entity
    private Payment toEntity(PaymentDTO dto) {
        Student student = new Student(dto.getStudentId()); // ID-only constructor
        List<Course> programs = dto.getProgramIds() != null
                ? dto.getProgramIds().stream().map(Course::new).collect(Collectors.toList())
                : List.of();

        Payment payment = new Payment();
        payment.setPaymentId(dto.getPaymentId());
        payment.setStudent(student);
        payment.setPrograms(programs);
        payment.setAmount(dto.getAdvanceAmount());
        payment.setTotalFee(dto.getTotalFee());
        payment.setPaymentDate(dto.getPaymentDate()); // LocalDate
        payment.setStatus(dto.getStatus());

        return payment;
    }

    // Entity -> DTO
    private PaymentDTO toDTO(Payment entity) {
        List<String> programIds = entity.getPrograms() != null
                ? entity.getPrograms().stream().map(Course::getProgramId).collect(Collectors.toList())
                : List.of();

        return new PaymentDTO(
                entity.getPaymentId(),
                entity.getStudent() != null ? entity.getStudent().getStudentId() : "",
                programIds,
                entity.getAmount(),
                entity.getTotalFee(),
                entity.getPaymentDate(),
                entity.getStatus()
        );
    }

    @Override
    public boolean updateStatus(String paymentId, String newStatus) throws Exception {
        Payment payment = paymentDAO.findById(paymentId);
        if (payment != null) {
            payment.setStatus(newStatus);
            return paymentDAO.update(payment);
        }
        return false;
    }
}
