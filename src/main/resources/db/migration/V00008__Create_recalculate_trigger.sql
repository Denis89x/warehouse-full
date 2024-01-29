create function recalculate_product_quantity() returns trigger
    language plpgsql
as
$$
DECLARE
    total_cost INTEGER;
BEGIN
    IF TG_OP = 'INSERT' THEN

        IF (SELECT ord_type FROM ord WHERE ord_id = NEW.ord_id) = 'Поступление' THEN
            UPDATE product
            SET presence = presence + NEW.quantity
            WHERE product_id = NEW.product_id;
        end if;

        IF (SELECT ord_type FROM ord WHERE ord_id = NEW.ord_id) = 'Выбытие' AND NEW.quantity > (SELECT presence FROM product WHERE product_id = NEW.product_id) THEN
            RAISE EXCEPTION 'Cannot have a "Выбытие" quantity greater than current presence for product %', NEW.product_id;
        END IF;

        IF (SELECT ord_type FROM ord WHERE ord_id = NEW.ord_id) = 'Выбытие' THEN
            UPDATE product
            SET presence = presence - NEW.quantity
            WHERE product_id = NEW.product_id;
        end if;

    ELSIF TG_OP = 'DELETE' THEN
        UPDATE product
        SET presence =
                CASE
                    WHEN (SELECT ord_type FROM ord WHERE ord_id = OLD.ord_id) = 'Поступление' THEN presence - OLD.quantity
                    WHEN (SELECT ord_type FROM ord WHERE ord_id = OLD.ord_id) = 'Выбытие' THEN presence + OLD.quantity
                    ELSE presence
                    END
        WHERE product_id = OLD.product_id;

    END IF;

    SELECT COALESCE(SUM(p.cost * oc.quantity), 0)
    INTO total_cost
    FROM order_composition oc
             JOIN product p ON oc.product_id = p.product_id
    WHERE oc.ord_id = NEW.ord_id;

    UPDATE ord
    SET amount = total_cost
    WHERE ord_id = NEW.ord_id;

    RETURN NULL;
END;
$$;

alter function recalculate_product_quantity() owner to postgres;

