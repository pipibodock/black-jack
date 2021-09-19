(ns black-jack.game
  (:require [card-ascii-art.core :as card]))

(defn new-card []
  "Genereta a card number between 1 and 13"
  (inc (rand-int 13)))

(defn player [player-name]
  (let [card1 (new-card)
        card2 (new-card)]
    {:player-name player-name
     :cards [card1 card2]}))

(card/print-player (player "luiz"))
