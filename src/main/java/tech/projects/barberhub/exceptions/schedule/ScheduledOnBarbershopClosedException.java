package tech.projects.barberhub.exceptions.schedule;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import tech.projects.barberhub.exceptions.BusinessException;

public class ScheduledOnBarbershopClosedException extends BusinessException {
    private final String message;
    public ScheduledOnBarbershopClosedException(String message) {
        this.message=message;
    }

    @Override
    public ProblemDetail toProblemDetail() {
        var problemDetail = ProblemDetail.forStatus(HttpStatus.UNPROCESSABLE_ENTITY);
        problemDetail.setTitle("Problem to schedule");
        problemDetail.setDetail(message);
        return problemDetail;
    }
}
