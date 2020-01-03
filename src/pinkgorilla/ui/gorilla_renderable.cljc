(ns pinkgorilla.ui.gorilla-renderable)

;;; This is the protocol that a type must implement if it wants to customise its rendering in Gorilla.
;;;  It defines a single function, render, that should transform the value into a value that the 
;;; front-end's renderer can display.

(defprotocol Renderable
  (render [self]))


;; RenderableJS allows rendering of javascript code.
;; Needed because script tags are not executed in react.

;; awb99: prefixes are necessary, because only "name" would overwrite clojure/name.
;; This should not happen, but it does.

;; TODO: RenderableJS CANNOT BE USED, SIMPLY BECAUSE THE UI RENDERING ONLY KNOWS HOW TO DEAL
;; WITH RENDERABLE. SO POSSIBLY WE NEED TO EXTEND RENDERABLEJS TO DEPEND ON RENDERABLE ???


(defprotocol RenderableJS
  (js-name [self]) ; a unique name that identifies the JS renderer
  (js-dependencies [self]) ; a vector of dependency-urls that need to be loaded prior to rendering
  (js-render [self]) ; javascript function body that will do the render part
  (js-cleanup [self])) ; javascript function body that will do cleanup when the render output is removed.
