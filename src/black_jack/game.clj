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

(defn game [player]
  (println (:player-name player) ": mais cartas?")
  (if (= (read-line) "sim")
    (let [player-with-more-cards (more-card player)]
      (card/print-player player-with-more-cards)
      (recur player-with-more-cards))
    player))

(defn dealer-decision [player-points dealer]
  (let [dealer-points (:points dealer)]
    (<= dealer-points player-points)))

(defn dealer-game [player-points dealer]
  (println (:player-name dealer) ": mais cartas?")
  (if (dealer-decision player-points dealer)
    (let [dealer-with-more-cards (more-card dealer)]
      (card/print-player dealer-with-more-cards)
      (recur player-points dealer-with-more-cards))
    dealer))

(def player-one (player "luiz"))
(card/print-player player-one)

(def dealer (player "dealer"))
(card/print-player dealer)

(def player-after-game (game player-one))
(dealer-game (:points player-after-game) dealer)
