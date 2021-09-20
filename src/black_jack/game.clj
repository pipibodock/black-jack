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
    (if (> player-points 21) false (<= dealer-points player-points))))

(defn dealer-game [player-points dealer]
  (if (dealer-decision player-points dealer)
    (let [dealer-with-more-cards (more-card dealer)]
      (card/print-player dealer-with-more-cards)
      (recur player-points dealer-with-more-cards))
    dealer))

(defn end-game [player dealer]
  (let [player-points (:points player)
        dealer-points (:points dealer)
        player-name (:player-name player)
        dealer-name (:player-name dealer)
        message (cond
                  (and (> player-points 21) (> dealer-points 21)) "Ambos Perderam"
                  (= player-points dealer-points) "Empatou"
                  (> player-points 21) (str dealer-name " ganhou")
                  (> dealer-points 21) (str player-name " ganhou")
                  (> player-points dealer-points) (str player-name " ganhou")
                  (> dealer-points player-points) (str dealer-name " ganhou"))]
    (card/print-player player)
    (card/print-player dealer)
    (println message)))

; ---------------------------------------------------
(def player-one (player "Luiz"))
(card/print-player player-one)

(def dealer (player "dealer"))
(card/print-masked-player dealer)

(def player-after-game (game player-one))
(def dealer-after-game (dealer-game (:points player-after-game) dealer))

(end-game player-after-game dealer-after-game)
