create function insert_if_not_exists(ime_smestajaa text, drzavaa text, gradd text, ocenaa double precision) returns smestaj
    language plpgsql
as
$$
declare result smestaj%rowtype;
begin
    insert into smestaj (ime_smestaja, drzava, grad, ocena)
    select ime_smestajaa, drzavaa, gradd, ocenaa
    where not exists (
        select 1 from smestaj s where s.ime_smestaja = ime_smestajaa and s.grad = gradd
    )
    RETURNING * INTO result;

    IF FOUND THEN
        RETURN result;
    END IF;

    SELECT * INTO result
    FROM smestaj s
    WHERE s.ime_smestaja = ime_smestajaa AND s.grad = gradd
    LIMIT 1;

    return result;
end;
$$;