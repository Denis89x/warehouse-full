CREATE OR REPLACE FUNCTION update_product_quantity()
    RETURNS TRIGGER AS
$$
DECLARE
    total_amount INT;
BEGIN
    IF TG_OP = 'INSERT' THEN
        IF (SELECT ord_type FROM ord WHERE ord_id = NEW.ord_id) = 'Поступление' THEN

            UPDATE product
            SET presence = presence + NEW.quantity
            WHERE product_id = NEW.product_id;
        ELSIF (SELECT ord_type FROM ord WHERE ord_id = NEW.ord_id) = 'Выбытие' THEN

            UPDATE product
            SET presence = presence - NEW.quantity
            WHERE product_id = NEW.product_id;
        END IF;
    ELSIF TG_OP = 'DELETE' THEN
        IF (SELECT ord_type FROM ord WHERE ord_id = OLD.ord_id) = 'Поступление' THEN

            UPDATE product
            SET presence = presence - OLD.quantity
            WHERE product_id = OLD.product_id;
        ELSIF (SELECT ord_type FROM ord WHERE ord_id = OLD.ord_id) = 'Выбытие' THEN

            UPDATE product
            SET presence = presence + OLD.quantity
            WHERE product_id = OLD.product_id;
        END IF;
    END IF;


    SELECT COALESCE(SUM(p.cost * oc.quantity), 0)
    INTO total_amount
    FROM order_composition oc
             JOIN product p ON oc.product_id = p.product_id
    WHERE oc.ord_id = NEW.ord_id;
    UPDATE ord
    SET amount = total_amount
    WHERE ord_id = NEW.ord_id;

    RETURN NULL;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER order_update_product_quantity
    AFTER INSERT OR DELETE
    ON order_composition
    FOR EACH ROW
EXECUTE FUNCTION update_product_quantity();

CREATE OR REPLACE FUNCTION delete_order_composition()
    RETURNS TRIGGER AS
$$
BEGIN
    DELETE
    FROM order_composition
    WHERE ord_id = OLD.ord_id;

    RETURN NULL;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER delete_order_composition_trigger
    AFTER DELETE
    ON ord
    FOR EACH ROW
EXECUTE FUNCTION delete_order_composition();
