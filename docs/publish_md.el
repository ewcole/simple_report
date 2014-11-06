;; see http://orgmode.org/worg/org-tutorials/org-publish-html-tutorial.html

(require 'org-publish)

(setq org-publish-cache nil) ;; Publish files whether they've changed or not.

(setq org-publish-project-alist 
      '(("org-md"
         :base-directory       "."
         :base-extension       "org"
         :publishing-directory "./md"
         :recursive            t
         :publishing-function  org-md-export-as-markdown
         :headline-levels      4
         :auto-preamble        nil
         :auto-sitemap         t
         :sitemap-filename     "sitemap.org"
         :sitemap-title        "Documentation"
         )
        ("org-html"
         :base-directory       "."
         :base-extension       "org"
         :publishing-directory "./html"
         :recursive            t
         :publishing-function  org-html-publish-to-html
         ;:exclude    "[pP]erformance\\\|[eE]valuation"
         :headline-levels      4
         :auto-preamble        t
         :auto-sitemap         nil
         ; :sitemap-filename     "sitemap.org"
         ; :sitemap-title        "Site Map"
         )
        ("org-static"
         :base-directory       "."
         :base-extension       "css\\|js\\|png\\|jpg\\|svg"
         :publishing-directory "./md"
         :recursive            t
         :publishing-function  org-publish-attachment)
        ("org" :components ("org-md" "org-html" "org-static")))
      )
(switch-to-buffer "*Messages*")
(org-publish-project "org") 
(message "**************************************************")
(message "** DONE                                         **")
(message "**************************************************")
;(save-buffers-kill-terminal)
