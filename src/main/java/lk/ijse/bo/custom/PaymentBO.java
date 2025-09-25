package lk.ijse.bo.custom;

import lk.ijse.bo.SuperBO;
import lk.ijse.dto.PaymentDTO;
import lk.ijse.dto.StudentDTO;
import lk.ijse.dto.CourseDTO;

import java.util.List;

public interface PaymentBO extends SuperBO {

    /**
     * Save a new payment.
     */
    boolean savePayment(PaymentDTO dto);

    /**
     * Update an existing payment.
     */
    boolean updatePayment(PaymentDTO dto);

    /**
     * Delete payment by ID.
     */
    boolean deletePayment(String id);

    /**
     * Get payment details by ID.
     */
    PaymentDTO getPayment(String id);

    /**
     * Get all payments.
     */
    List<PaymentDTO> getAllPayments();

    /**
     * Generate a new unique payment ID.
     */
    String generatePaymentId();

    /**
     * Load all students for combo box or related data.
     */
    List<StudentDTO> getAllStudents();

    /**
     * Load all programs/courses for combo box or related data.
     */
    List<CourseDTO> getAllPrograms();

    /**
     * Update only the status of a payment (e.g., PAID, PARTIAL, PENDING).
     */
    boolean updateStatus(String paymentId, String newStatus) throws Exception;
}
