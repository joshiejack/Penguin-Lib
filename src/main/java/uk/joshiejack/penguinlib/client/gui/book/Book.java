package uk.joshiejack.penguinlib.client.gui.book;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import uk.joshiejack.penguinlib.client.gui.AbstractContainerScreen;
import uk.joshiejack.penguinlib.client.gui.book.page.AbstractPage;
import uk.joshiejack.penguinlib.client.gui.book.tab.Tab;
import uk.joshiejack.penguinlib.inventory.AbstractBookContainer;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@OnlyIn(Dist.CLIENT)
@SuppressWarnings("ConstantConditions")
public class Book extends AbstractContainerScreen<AbstractBookContainer> {
    private static final Object2ObjectMap<String, Book> BOOK_INSTANCES = new Object2ObjectOpenHashMap<>();
    private final List<Tab> tabs = new ArrayList<>();
    private final ResourceLocation backgroundL;
    private final ResourceLocation backgroundR;
    private int centre, bgLeftOffset;
    private Tab defaultTab = Tab.EMPTY;
    private Tab tab;
    public int fontColor1 = 0x857754;
    public int fontColor2 = 4210752;
    public int lineColor1 = 0xFFB0A483;
    public int lineColor2 = 0xFF9C8C63;

    public Book(String modid, AbstractBookContainer container, PlayerInventory inv, ITextComponent name) {
        super(container, inv, name, null, 360, 230);
        backgroundL = new ResourceLocation(modid, "textures/gui/book_left.png");
        backgroundR = new ResourceLocation(modid, "textures/gui/book_right.png");
    }

    public static Book getInstance(String modid, AbstractBookContainer container, PlayerInventory inv, ITextComponent name, Consumer<Book> consumer) {
        if (!BOOK_INSTANCES.containsKey(modid)) {
            Book screen = new Book(modid, container, inv, name);
            consumer.accept(screen); //Apply extra data to this bookscreen
            BOOK_INSTANCES.put(modid, screen);
        }

        return BOOK_INSTANCES.get(modid);
    }

    @Nonnull
    @Override
    public <T extends Widget> T addButton(@Nonnull T button) {
        return super.addButton(button);
    }

    @Nonnull
    @Override
    public <T extends IGuiEventListener> T addWidget(@Nonnull T widget) {
        return super.addWidget(widget);
    }

    public void setTab(Tab tab) {
        this.tab = tab;
        this.init(minecraft, width, height);
    }

    public void bindLeftTexture() {
        minecraft.getTextureManager().bind(backgroundL);
    }

    /**
     * Add a page to this book, automatically creates a tab for the page on the left side of the book
     **/
    public Tab withTab(Tab tab) {
        if (!tabs.contains(tab))
            tabs.add(tab);
        if (defaultTab == Tab.EMPTY)
            defaultTab = tab;
        return tab;
    }

    public Tab getTab() {
        return tab;
    }

    public Minecraft minecraft() {
        return minecraft;
    }

    public boolean isSelected(AbstractPage page) {
        return this.tab.getPage() == page;
    }

    public boolean isSelected(Tab tab) {
        return this.tab == tab;
    }

    @Override
    public void init() {
        super.init();
        leftPos = (this.width - this.imageWidth) / 2;
        topPos = ((this.height - this.imageHeight) / 2) + 10;
        centre = leftPos + (imageWidth / 2);
        bgLeftOffset = centre - 154;
        titleLabelX = (imageWidth / 2) - font.width(title) / 2;
        titleLabelY = -10;
        if (tab == null)
            tab = defaultTab;

        tab.getPage().initLeft(this, bgLeftOffset, 15 + topPos);
        tab.getPage().initRight(this, centre, 15 + topPos);
        tab.addTabs(this, centre + 154, 15 + topPos);
        if (tabs.size() > 1) {
            int y = 0;
            for (Tab tab : tabs)
                addButton(tab.create(this, centre - 180, 15 + topPos + (y++ * 36)));
        }
    }

    @Override
    protected void renderBg(@Nonnull MatrixStack matrix, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.color4f(1F, 1F, 1F, 1F);
        renderBackground(matrix);
        minecraft.getTextureManager().bind(backgroundL);
        blit(matrix, bgLeftOffset, topPos, 102, 0, 154, 202);
        minecraft.getTextureManager().bind(backgroundR);
        blit(matrix, centre, topPos, 0, 0, 154, 202);
    }

    @Override
    protected void renderLabels(@Nonnull MatrixStack matrix, int x, int y) {
        font.drawShadow(matrix, title, (float) titleLabelX, (float) titleLabelY, 0xFFFFFF);
    }
}