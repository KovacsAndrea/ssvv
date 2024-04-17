package validation;

import domain.Student;

public class StudentValidator implements Validator<Student> {

    /**
     * Valideaza un student
     * @param entity - studentul pe care il valideaza
     * @throws ValidationException - daca studentul nu e valid
     */
    @Override
    public void validate(Student entity) throws ValidationException {
        if(entity.getID().equals("")){
            throw new ValidationException("Id incorect!");
        }
        if(entity.getID() == null){
            throw new ValidationException("Id incorect!");
        }
        if(entity.getNume() == ""){
            throw new ValidationException("Nume incorect!");
        }
        if(entity.getGrupa() < 100 || entity.getGrupa() > 9999) {
            throw new ValidationException("Grupa incorecta! Grupa trebuie sa aiba intre 3 si 4 cifre.");
        }
        if(entity.getEmail() == null){
            throw new ValidationException("Email incorect!");
        }
        if(entity.getNume() == null){
            throw new ValidationException("Nume incorect!");
        }
        if(entity.getEmail().equals("")){
            throw new ValidationException("Email gol!");
        }

        if(!entity.getEmail().matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")){
            throw new ValidationException("Email incorect! Email-ul trebuie sa fie de forma ceva@ceva.ceva");
        }
    }
}
