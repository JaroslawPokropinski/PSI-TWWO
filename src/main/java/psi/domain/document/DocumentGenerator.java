package psi.domain.document;

public interface DocumentGenerator<T> {

    Document generateDocument(T entity);

}
