(org-export-define-derived-backend 'genenakagaki 'html
  :translate-alist '((template . my-latex-template))
  :menu-entry
  '(?g "Export to hiccup"
       ((?G "As hiccup buffer" gn/export-to-buffer)
	(?g "As hiccup file" gn/export-to-file)
	(?o "As hiccup file and open"
	    (lambda (a s v b)
	      (if a (gn/export-to-file t s v b)
		(org-open-file (gn/export-to-file nil s v b)))))))
  :options-alist '((:html-head-include-default-style
                    nil nil nil)))

(defun gn/export-to-buffer
    (&optional async subtreep visible-only body-only ext-plist)
  (interactive)
  (org-export-to-buffer 'genenakagaki "*Org genenakagaki.com Export*"
    async subtreep visible-only body-only ext-plist (lambda () (set-auto-mode t))))

(defun gn/export-to-file
    (&optional async subtreep visible-only body-only ext-plist)
  (interactive)
  (let* ((extension ".html")
	 (file (org-export-output-file-name extension subtreep))
	 (org-export-coding-system org-html-coding-system))
    (org-export-to-file 'genenakagaki file
      async subtreep visible-only body-only ext-plist)))

(customize-set-variable 'org-html-doctype "html5")
(customize-set-variable 'org-html-html5-fancy t)
(customize-set-variable 'org-html-head-include-default-style nil)
(customize-set-variable 'org-html-head-include-scripts nil)
(customize-set-variable 'org-html-viewport '())
(customize-set-variable 'org-html-head "
<meta charset=\"UTF-8\">
<meta name=\"description\" content=\"Free Web tutorials\">
")
(customize-set-variable 'org-html-preamble nil)
(customize-set-variable 'org-html-postamble nil)
(customize-set-variable 'org-html-footnotes-section "")

(setq org-publish-project-alist
      '(("genenakagaki.com"
         :base-directory "~/genenakagaki.com/org/"
         :base-extension "org"
         :publishing-directory "~/genenakagaki.com/page-view-converter/resources/pages/"
         :recursive t
         :publishing-function org-html-publish-to-html
         :headline-levels 4
         :html-head-include-default-style nil
         )))

(provide 'gn-setup-genenakagaki-export)
