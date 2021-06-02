(ns picasso.default-config
  "side effects for Renderable types"
  (:require
   [picasso.protocols]

   ; render
   #?(:clj [picasso.render.clj-types])
   #?(:cljs [picasso.render.cljs-types])
   #?(:clj [picasso.render.image])


   ; paint


   [picasso.paint.default]
   [picasso.paint.hiccup]
   #?(:cljs [picasso.paint.html-text])
   [picasso.paint.list-like]
   #?(:cljs [picasso.paint.pinkie])

   ;
   ))