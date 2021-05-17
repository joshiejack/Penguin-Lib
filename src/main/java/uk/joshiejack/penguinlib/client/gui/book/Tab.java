package uk.joshiejack.penguinlib.client.gui.book;

import joptsimple.internal.Strings;
import net.minecraft.client.gui.widget.button.AbstractButton;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import uk.joshiejack.penguinlib.client.gui.book.widget.TabButton;
import uk.joshiejack.penguinlib.client.gui.book.page.AbstractPage;

import java.util.ArrayList;
import java.util.List;

public class Tab {
    public static final ITextComponent EMPTY_STRING = new StringTextComponent(Strings.EMPTY);
    public static final Tab EMPTY = new Tab(EMPTY_STRING);
    private final List<AbstractPage> pages = new ArrayList<>();
    private final ITextComponent name;
    private AbstractPage defaultPage = AbstractPage.EMPTY;
    private AbstractPage page;

    public Tab(ITextComponent name) {
        this.name = name;
    }

    public Tab withPage(AbstractPage page) {
        if (!pages.contains(page))
            pages.add(page);
        if (defaultPage == AbstractPage.EMPTY)
            defaultPage = page;
        return this;
    }

    public AbstractPage getPage() {
        if (page == null)
            page = defaultPage;
        return page;
    }

    public void setPage(AbstractPage page) {
        this.page = page;
    }

    public AbstractButton create(Book screen, int x, int y) {
        Button.ITooltip tooltip = (btn, mtx, mX, mY) -> screen.renderTooltip(mtx,
                screen.minecraft().font.split(name, Math.max(screen.width / 2 - 43, 170)), mX, mY);
        Button.IPressable action = (btn) -> {
            if (page == null)
                page = defaultPage;
            screen.setTab(this); //Refresh
        };

        //Creates the tab for this page
        return new TabButton.Left(screen, x, y, name, action, tooltip, screen.isSelected(this));
    }

    public void addTabs(Book screen, int x, int y) {
        if (pages.size() > 1) {
            int i = 0;
            for (AbstractPage page : pages)
                screen.addButton(page.createTab(screen, this, x, y + (i++ * 36)));
        }
    }
}