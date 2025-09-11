package lk.ijse.dao;

import lk.ijse.dao.custom.impl.*;


public class DAOFactory {

    public enum DAOType{
        PROGRAM,STUDENT,QUERY,ENROLLMENT,USER,COURSE,INSTRUCTOR,LESSON,PAYMENT
    }

    public static SuperDAO getDAO(DAOType daoType){
        return switch (daoType) {
            case PROGRAM -> new CourseDAOImpl();
            case STUDENT -> new StudentDAOImpl();
            case QUERY -> new QueryDAOImpl();
            case INSTRUCTOR -> new InstructorDAOImpl();
            case USER -> new UserDAOImpl();
            case COURSE -> new CourseDAOImpl();
            case LESSON -> new LessonDAOImpl();
            case PAYMENT -> new PaymentDAOImpl();
            default -> null;
        };
    }
}
