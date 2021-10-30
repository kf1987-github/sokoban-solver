                        Sokoban for Windows95/98/NT/2000.

                                A brief description


Installation
        No special actions required. Just copy executable to any folder
        and start it from there.

Uninstalling
        Program writes some data into registry:
                HKEY_CURRENT_USER\Software\YGP\YSokoban

        To uninstall the program - select uninstall from system menu and click
        OK in a dialog box which informs you that it will clear registry.
        Program will clear registry settings and will exit.
        Remove executable and all related level and player files.
        If you change your mind you can start program again, but you should
        make again all settings from options dialog.

Usage:

  Playing (solving) level:

        arrow keys - move person (and push boxes)

        mouse - left mouse button is used normally to select box (ball) for
                moving and then to click on position where we want this box
                to be moved. You can configure how exactly you want to use
                right & middle mouse buttons (normally as undo and redo).

                See optimizing push path options for fine tune optimization
                of a push path.


        ESC - start same level from the beginning (enlarge window size if
              level does not fit, or center level if it is smaller).

        Shift-ESC - same as above but will shrink dialog box if needed.

        backspace - undo (unlimited)

        space - redo

        You can configure what to do when you click left/right/middle mouse
        buttons. And what to do on mouse wheel move. So specify how to get
        undo/redo with mouse.

        Pressing left & right mouse buttons (or left button with shift) will
        always select object (or move player is clicking on empty cell),
        regardless of what is configured for mouse buttons.

  Hints:
        F9 - hint mode on/off - shows where selected person (box)
                                can go (be pushed).

        Ctrl-F9 - hint to show non pushable box (toggle).

        In options dialog there is a checkbox - show non pushable boxes (mouse).
        When you check it, then while holding left mouse button down on wall
        you will see all pushable and non pushable boxes
        (if Ctrl-F9 hint is off).

  Skins control:
        F4 - select internal skin (suitable for vga 16 color 640x480)
        F5\
        F6 \  select custom skin (4 possible skins are available)
        F7 /  (you can set which files to use from options dialog)
        F8/

  Support for packed solutions & levels
        Sokoban supports paste (shift-Ins or ctrl-V) of packed solution.
        Packed solution is like normal lurd format solution but with RLE
        (run-length-encoding).

        For example you can paste RLE packed level (| means new line):

        2_5#|3#3_#|#2_*#_2#|#_#2_*_#|#_*2_#_#|2#_#+2_#|_#3_$2#|_3#2_#|3_4#

        or you can paste RLE packed solution:

        2r2uL2u2lD2l2d2Rl2d2rdrU3l2ul2u2rD2u2r2dL2D2u2r2dLd
        L2Ud2r2ul2u2ld2l2dr2dRl2ul2u2ru2r2dr2d2luLUR2dr2dlU

        Sokoban will create a RLE packed level and solution with ctrl+shift-L
        There is an option (see options dialog) how to encode space -
          with underscore '_' or with hyphen '-'
        Both characters are supported for import (even space is possible).

  Replay solution & Copy/Paste solution:

        F2 - Every successful solution is recorded in a user description file.
             With this button program will play back this solution.
             Program recognize levels even if they are rotated, flipped or
             has additional walls that does not change the level (dead walls).
             Or if the man is at the different position, but could step to
             original one without pushing any boxes.
             Take a look at the bottom right corner (Your best result).
             If it is empty then you did not solve this level.
             If it is not empty - you already solve it and you can
             replay solution.

        ctrl-F2 - same as F2 (play solution) but with breaks,
                  continue to next box pushing with space
                  (ctrl-F2 simply fills redo buffer).
                  F2 can be paused by pressing space and then run
                  again by pressing ctrl-space.

        replay speed:
                while you are holding ctrl down (during replay/redo) action
                will go 2 times faster, if you hold shift - 4 times faster,
                while you hold ctrl&shift - will go at maximum rate.

        Instant move:
                There is a button <IM>. When it is pressed (down) man will move
                instantly and there will be now animation of moving. This was
                requested by Ming for easy handle of very huge levels (when man
                has to made hundred moves to push some box).
                See option: "Show dir change during IM".

        To be able to send solution to a friend, or use such a solution.
        ctrl-L  - copy (in a clipboard) current level (in xsb format) plus
                  solutions (start & end line are lines where this level
                  is located in orignal file). Moves (if any) are moves
                  made during this session.

        ctrl-alt-L - same as ctrl-L but spaces are replaced with - or _

        ctrl-shift-L - same as ctrl-L but RLE packed

        ctrl-alt-shift-L - same as ctrl-L but MF8 format, when pasting there
                           should be [soko= or [/soko] somewhere to accept
                           it as MF8

        ctrl-M - same as above but has XSB + MF8 format

        ctrl-Ins (or ctrl-C)  - copy moves made for this level to clipboard
                                (format: udlrUDLR)
                                Note: If you have already solved some level
                                then to copy the solution (without solving
                                again) - first press F2 - play it and then
                                press ctrl-C

        shift-Ins (or ctrl-V) - paste moves from clipboard (format: udlrUDLR)

        ctrl-shift-V (ctrl-shift-Ins) - same as above (paste moves) but stops
                                        on every box change then you can
                                        continue with space. In fact it just
                                        fills up redo buffer
                for example: Press ctrl-C (or ctrl-Ins) after you finish the
                             level (but before moving to the next one).
                             All moves will be
                             copied to the clipboard in udlr format.

                Now you can paste them into e-mail (or some other text file).

                If you receive such a solution. Mark and copy it to the
                clipboard. Go to the desired level and press shift-Ins
                (ctrl-V) and solution will be played (if it is correct).
                If you want to see solution with pause between
                box changes, then press ctrl-shift-V (ctrl-shift-Ins).

                playing can be paused by pressing space and then run
                again by pressing ctrl-space.


        ctrl-alt-C (ctrl-alt-Ins): copy to clipboard the rest of the solution
                if you are currently performing a step by step review of solution
                Requested by Ming with following explanation:

                when replaying solutions (tracing the moves one by one),
                at any given time, ctrl+c will copy the current solution (from
                beginning up to the current move) to the clipboard.
                It would be useful to have a function copy the rest
                of the move (those moves after the current position) to the clipboard.
                This function comes in handy when one wants to improve
                their existing solution(s).
                Often, one need to trace both solutions when and then get to a common
                map; then perform the rest of the moves  (if this feature is available,
                one can just copy and paste it)

        Save/Load solution state: ctrl-S / ctrl-D will save/load current level and
                moves up to now. You have to specify filename to save/load level and solutions.
                
  Ruler:
        Ctrl-R / Alt-R to toggle letter/number ruler on/off.
                letter ruler is chess like notation. Number is numbers only.

  Paste level:
        You can paste sokoban level (or entire collection). If there is at
        least one # in a clipboard, then it is considered as a sokoban
        level/collection - not as a solution.

  Bookmarking levels:
        Ctrl-digit will bookmark level. digit (alone) will jump to bookmarked level.

  Select collection:
        Select item "*** select ****" in combo box or just push Collection
        button and then select a file which contains collection
        (or a single level).

        Ctrl-Tab - will drop down Collection combo box and you can
                   quickly select collection.

        Shift-Tab - will drop down Solution combo box (if you have more than
                    one solution) and you can quickly select collection.

  Select player: same as with collections. Player file contains
        solution for all levels that you play. To create new player -
        specify file that does not exist and it will be created.
        Or if you play several levels with default player - just
        rename default.SokoInfo to yourname.SokoInfo


  Change level:
        If you successfully finish some level (you got a congratulations)
        then just press        enter to go to the next level (or left mouse click
        on any empty position).

        Ctrl-Home/End - go to first/last level of Collection.

        Ctrl-arrows (left/right or up/down) go to prev/next level

        Ctrl-Shift-arrows - go to prev/next unsolved level
                            (such that "Your best result" is empty).

        Ctrl-Alt-arrows - go to prev/next incorrect level
               (number of boxes do not match number of targets)

  Transform level:
          It is possible to transform level during play. Click on button next
          to level number (normally it displays 'O' - original). Then you can
          select how to transform the level. Shortcuts to transform level are:
          Shift - L,R,U,/,\,O,H,V - will transform level. Pressing second time
          same button will have no result. For example if you press Shift-L
          level will be transformed to "left rotated", so if you press Shift-L
          again it will not be transformed again - it is already "left rotated".
          To rotate several times level use Shift arrows - Shift-left arrow will
          rotate level to the left (from curront position), so pressing it again
          will rotate it once again. Shift-right arrow will rotate level to the
          right. Shift - up arrow, and down arrow will mirror level vertically
          and horizontaly.


  Mouse settings:

        From options dialog you can select what action to take when
        clicking on empty position
          - move bird (man ...)
          - Show all balls that bird could push to this position
            (if this is a target position)
          - Show all balls that bird could push to this position
            (empty cell or target position)

        When above option is turned on then it is possible to auto select
        nearest ball. When auto selecting ball program will select only
        balls that are not already on target positions.

        When above option is turned on for left mouse click, then it is
        impossible to move (or even select) person (if you specify that this
        should happen on left mouse click). If you want to select person (man
        or bird) for moving then click on it with both mouse buttons.
        Normally there is no need to select bird for moving.

        This is very usefully when finishing level - there are lot of balls to
        push to target positions, but it's nearly obvious what should be done.
        In that case you should just click on target position - program will
        auto select nearest ball and with next click (with same mouse button
        on same cell) man will push auto selected ball to this position.
        Just try it, it's much easier to solve levels with that feature.

        Suggestions about mouse settings:

        1. If you have mouse wheel - select wheel to do undo/redo and select
           right mouse button to show pushable balls.

          2. If you have 3 button mouse: possible selections are:
             - left mouse to show pushable balls, middle button to redo,
               right to undo
             - left mouse to select move person, middle to show pushable balls,
               right to undo

          3. If you have 2 button mouse:
             - left mouse to show pushable balls, right to undo

  Scrollbars
        If level is too big then YSokoban will show scrollbars (always both).
        If you do not define mouse wheel as undo/redo then mouse wheel will
        move scrollbars (vertical, if not enabled then horisontal).
        Ctrl/Shift with mouse wheel will always (even if wheel is defined
        as undo/redo) move vertical/horizontal scrollbar (if enabled).

        Auto follow - there is a small button between scrollbars.
        When pressed - YSokoban will do autofollow (move scrollbars
        automatically). If man is working (very long move or playing
        solution) and you move scrollbars manually - then autofollow will
        be automatically temporarly suppresed. On next move it will be
        again restored. If you want to activate it again press this button
        again (i.e. solution is played - you move scrollbars - autofollow
        stops - press button and it will autofollow again).

        On every level load (pressing ESC, or loading it for any othe reason)
        YSokoban will try to autofit it's size according to level size.
        If you do not want it - uncheck autoshrink/autoenlarge options.
        Usually sokoban levels are small enough and fit screen, so you will
        need this options unchecked only if you play huge levels.
        
        If scrollbars are visile then you can pan with mouse. Pressing down mouse
        button on any cell without man or box and then drag with mouse.

  Support for resizable skins:
        Any skin can be used as a resizable skin, but smaller skins doesn't
        look good when enlarged. It's better to have large skin and resize
        it down to smaller size. So all skins with size more than 40 pixels
        are considered resizable by default. Following keys control resizing:
        *        toggles autoresizing on/off.
        =       switch autoresizing off and set skin to it's default size.
        # or `  toggles autoresizing algorithm (there are two possible algorithms
                it depends on skin which one is better).

        - or [        stop autoresizing and use skin with one pixel less size than current.
        + or ]        stop autoresizing and use skin with one pixel greater size than current.        
                
Settings:

        Options dialog is activated from system menu:
        - Press alt-space or click with mouse on programs top left corner.
        - Since version 1.601 there is a menu button in Transform dialog.
        - It is possible to activate menu with F10.
        - You can activate menu by clicking on "Label:" (just before
        level edit box, and just below system icon). I know, it is not
        obvious, but think for it as that system menu (although it is
        not exactly system menu) is activated even if clicking just
        below system icon too. All this alternatives to activate menu
        are used in full screen mode or if you run YSokoban with Wine
        under Linux.

        Select skins from system menu.

        If you select checkbox "Count push as push + move" which is in
        Options dialog, then program will just show push/move counters as
        specified. This checkbox is independent from checkbox with same
        label in "Push path optimization" dialog.

        Show net in a level: Usable only for new style skins.
                New skins has no net inside them and net is drawn latter.
                So you can choose whether you want to see it or not.
                Ctrl-Fx (x depends on skin you load) does the opposite.
                For example if you load skin with F6 then Ctrl-F6 will
                select same skin but without (with) net.

        Auto shrink size if level is smaller. If checked will automatically
                shrink size of a level when loaded. If this checkbox is not
                selected, you should manually change size of window or you
                can press Shift-ESC.

        Go through boxes: If selected then man will go to selected place
                even if it is not directly reachable, but could be reached
                if some box is moved away from man's path and then pushed
                back to the same place. If you select to see hints (F9)
                then all direct accessible places are marked with white
                circles and all that are not directly accessable with squares.
                In this mode it is possible to push boxes which are not
                directly accessible. Selecting/showing boxes that could
                be pushed to some place will use go through possibilities.
                Hint controlled by ctrl-F9 (show reachable boxes) will
                display direct and indirect reachable (with go through) boxes.

                Note: Go through is not used during push path finding,
                this means that go throught is used only to move man somewhere
                (without pushing any box) or to make first push for some box,
                i.e. position man before starting to push boxes.

        How to paint squares outside of a level:
                Use skin - will use skin to paint outside of level (always
                        without net for new style skins).
                Use skin color - will use skin color (middle of empty skin)
                        to paint outside level.
                Use dialog color - will use standard windows dialog box color.

        Show dir change during IM:
                if selected will show position of a man when it changes pushing
                direction during IM (instant move mode).

        Show level size (in title):
                if selected will show level size (x, y) of a level in a title bar

        Solution should have at least one push:
                if on will not consider level which starts in a solved position
                as already solved. There are only few levels which depend on this
                setting. For example (copy/paste into ysocoban if you want to
                play them or just specify this file as a collection):

                         ####
                         #  #
                        ##  ######
                        #  *#@*  #
                        #    **  #
                        ##  #  ###
                         ####  #
                            ####

                            ####
                            #  #
                        #####  #
                        ##   **###
                        #  **@*  #
                        #    **  #
                        ###  * ###
                          ##   #
                           #####



Optimizing push path:

        Select "Push path optimization" (from system menu).

        With this settings you can fine tune the optimization criteria for
        push path. By default (if "Use weights" check box is not checked)
        program optimizes push path by number of pushes. These means that
        program        will use the path which has smallest number of pushes
        (there could be a lot of moves but the number of pushes is smallest).
        If there are more than one path (with same number of pushes) program
        will select these one which has less moves.

        If you select (check) "Use weights" then program will find the path
        for which the formula has lowest value. Perhaps you will prefer
        to optimize only number of moves, but if you wish you can select
        more complex criteria.
        If you select 0 as weight for moves and 0 as weight for inlines then
        program will automatically deselect "Use weights" and will use default
        optimization (because X, 0, 0 is default optimization regardless of
        X value).

        Check box "Count push as push + move" means that every push is
        counted as move too. For optimization procedure you could specify
        that they are independent (otherwise moves are always more than
        pushes). This option is only for push path optimizer and is
        independent from checkbox with same meaning in Options dialog box.

        In any case if there are two paths with same (lowest) value for
        optimization formula then program will select this one which has
        smallest number of pushes and if they are equal, then smallest number
        of moves and if they are equal then smallest number of inline pushes
        and if they are equal, then it will just pick one of them.


Creating collections:
        If you have some levels as a separate text files, just copy all of
        them in one file. Separate levels with empty lines or some other text
        (whatever you like).

        Program supports three keywords: Author, Title and Mouse

                Author: Name     -- to specify author's name

                Title: Some text -- to specify level (or collection) title

                Mouse: On/Off    -- to disable mouse (for entire collection
                                    or for some level). This is useful for
                                    very easy levels that should be solved
                                    by kids.
                                    And you want that they use kbd not mouse.

Creating skins: For those of you who wants to make skins.
        Skin is a regular bmp file. Use window's "Paint" to edit it.
        There are two kind skins
                1. old style contains 4x10 squares.
                2. new style contains 4x8 squares.
        It's obvious what should be in old style skins and nearly obvious
        what should be in new style skins. The only exception (for new style)
        are two white squares. First one (on the left) is not used, you can put
        there whatever you want. Second one (on the right) could be either
        white or should contain some special kind of dots. If it is filled
        with some color, then program assumes that all squares may have net
        and that all walls are constructed by dividing wall squares into 4
        parts and then constructing more complicated walls out of this parts.
        If this defaults are not appropriate then there should be a line
        (horizontal and vertical) to mark how should simple wall be divided
        For example: if skin size is 40 and there is a horizontal line with
        length 7 and vertical line with 11 points - these means: simple walls
        are divided into 4 rectangular areas: 7x11, 33x11, 7x29, 33x29.
        and then more complicated walls are constructed with them.
        This is use full if center of a cross wall (and of vertical and
        horizontal wall) is not in the center of square. Color which is
        used to make this marks is used as net color.
        With offset 1x1 pixel in this square there should be
        rectangle 4x8 pixel with same color which mark squares
        that may have net in them (when net option is selected).


Collections:
        Creating a level for Sokoban is not an easy task. I can't made levels.
        But you can search in Internet. On my Sokoban's (click on Sokoban in
        my home page http://welcome.to/ygp) page I put several links to web
        pages with levels.

        All sokoban levels (*.xsb, *.dat or *.txt files) are normal text files
        I recommend to take a look in every collection with a normal text
        editor. You can find there additional information about author,
        collection and levels. Sometimes authors put in there comments for
        their levels or collections.

        You can start YSokoban with a filename of a level as a parameter to
        a program. This functionality can be used to specify that double
        clicking on XSB file should start YSokoban with this level. To do
        that - double click on a xsb file (in windows explorer) if you do
        have no associations for that kind of filename you will get a
        dialog box with title "Open with". Click other button find YSokoban
        program and select it. Now every time you double click on xsb file
        it will start YSokoban to play it.

Solutions:
        Exporting solutions:
        Select "Export collection solutions" from menu.
        Specify filename to export solutions. You wil get one file with all
        levels with solutions. If you put % somewhere in filename,
        then levels (and solutions) will be exported one level per file.
        % will be replaced with level number in filename.

        Accesing solution on WEB:
        Now there is a list of solutions on YSokoban.atspace.com.
        If you want to search for a level, then you can go dirctly there.
        If level is from public collection (available for free in net) then
        you can try to find it in a list of collections. If it is there but
        there is no direct link to the list with levels, then that means that
        collection is perhaps not publis. You can try to find level by using
        it's hash code. Hascode is a unique code which YSokoban calculates
        and uses to name the page with solutions on this site. Hashcode is
        is not unique, it could happen that two levels has same hashcode,
        but usually this does not happen. Please take a closer look, maybe
        your level is rotated, mirrored, or man is moved to a different place,
        even some non important walls are added or removed. In that case you
        have to adapt the solution.

        Ctrl-F12 - gives you an URL which you can use with any browser.
        F12 - will try to start your default browser with this URL. If level
        is found, you will see it, if not, then perhaps you will see an error
        page, or home page of atspace.com. This means that there is still not
        such level in the list of levels.

Importing/exporting solutions:
        YSokoban can import solutions which were exported by YSokoban. The idea
        is to export solution from one player and then import them in another
        player. For example if you play it on more than one computers. This is
        some way of merging solutions of more than one player.
        If you want to import solutions exported by some other sokoban clone,
        then file should have solutions and levels in a text format. Solutions
        should be in LURD format (not RLE encoded, only pasting supports
        RLE encoding of solutions) and should have Solution keyword (with ':')
        at the beginning of a solution. Solution could be on more than one line.
        It is possible to have some info (skipped by program) after Solution
        keyword and before ':' enclosed in parenthesis. For exmaple:
        
            Solution: RRRurrrruulllUlluLLLullddrUrrrrdrrdrrrddlllldlll ...
        or
            Solution (1065/3622): RRRurrrruulllUlluLLLullddrUrrrrdrrdrr ...
        
        
For any problems (except - how to solve some level) drop me a letter on
        YSokoban@gmail.com


Any help to make this document more readable and error free
(my native language is not English) is welcome.


Enjoy
(And if you really enjoy and continue to play please send me an e-mail).

George Petrov