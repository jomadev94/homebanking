package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;

public class CreateCardDTO {

    private CardColor color;
    private CardType type;

    public CreateCardDTO(){}

    public CreateCardDTO(CardColor color, CardType type) {
        this.color = color;
        this.type = type;
    }

    public CardColor getColor() {
        return color;
    }

    public void setColor(CardColor color) {
        this.color = color;
    }

    public CardType getType() {
        return type;
    }

    public void setType(CardType type) {
        this.type = type;
    }
}
