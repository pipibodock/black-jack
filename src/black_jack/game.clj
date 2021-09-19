(ns black-jack.game
  (:require [card-ascii-art.core :as card]))

(defn new-card []
  "Genereta a card number between 1 and 13"
  (inc (rand-int 13)))

(defn jqk-to-ten [value]
  "Alter value J Q and K to 10"
  (if (> value 10) 10 value))

(defn one-to-eleven [value]
  "Alter value A to 11"
  (if (= value 1) 11 value))

(defn points-cards [cards]
  (let [cards-without-jqk (map jqk-to-ten cards)
        cards-without-as (map one-to-eleven cards-without-jqk)
        sum-with-as-one (reduce + cards-without-jqk)
        sum-with-as-eleven (reduce + cards-without-as)]
    (if (> sum-with-as-eleven 21) sum-with-as-one sum-with-as-eleven)))

(defn player [player-name]
  (let [card1 (new-card)
        card2 (new-card)
        cards [card1 card2]
        points (points-cards cards)]
    {:player-name player-name
     :cards cards
     :points points}))

(defn more-card [player]
  (let [card (new-card)
        cards (conj (:cards player) card)
        ;new-player (assoc player :cards cards) ;Alternativa ao update
        new-player (update player :cards conj card)
        points (points-cards cards)]
    (assoc new-player :points points)))

(def player (player "luiz"))
(card/print-player (more-card player))

;(card/print-player (player "luiz"))
;(card/print-player (player "dealer"))
