package com.primesys.VehicalTracking.MyAdpter;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.primesys.VehicalTracking.Dto.AssociatedParentDTO;
import com.primesys.VehicalTracking.R;
import com.primesys.VehicalTracking.Utility.FlipAnimator;

import java.util.ArrayList;
import java.util.List;


public class AllGroupRecyclerviewAdpter extends RecyclerView.Adapter<AllGroupRecyclerviewAdpter.MyViewHolder> {
    private Context mContext;
    private List<AssociatedParentDTO> parentListData;
    private RecyleAdapterListener listener;
    private SparseBooleanArray selectedItems;

    // array used to perform multiple animation at once
    private SparseBooleanArray animationItemsIndex;
    private boolean reverseAllAnimations = false;

    // index is used to animate only the selected row
    // dirty fix, find a better solution
    private static int currentSelectedIndex = -1;



    public AllGroupRecyclerviewAdpter(Context mContext, ArrayList<AssociatedParentDTO> parentListData, RecyleAdapterListener listener) {
        this.mContext = mContext;
        this.parentListData = parentListData;
        this.listener = listener;
        selectedItems = new SparseBooleanArray();
        animationItemsIndex = new SparseBooleanArray();
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_group_rowdata, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        AssociatedParentDTO message = parentListData.get(position);

        // displaying text view data
        holder.txt_parentname.setText(message.getName().trim());
     //   holder.txt_time.setText(message.getTime());
        holder.txt_total_child.setText("Total device associated :"+message.getTotalChild().trim());

        // displaying the first letter of From in icon text
        holder.iconText.setText(message.getName().substring(0, 1).toUpperCase());

        // change the row state to activated
        holder.itemView.setActivated(selectedItems.get(position, false));

        // change the font style depending on message read status
      //  applyReadStatus(holder, message);

        // handle message star
    //    applyImportant(holder, message);

        // handle icon animation
        applyIconAnimation(holder, position);

        // display profile image
        applyProfilePicture(holder, message);

        // apply click events
        applyClickEvents(holder, position);
    }

    private void applyClickEvents(MyViewHolder holder, final int position) {
        holder.iconContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onIconClicked(position);
            }
        });

       /* holder.iconImp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onIconImportantClicked(position);
            }
        });*/

        holder.messageContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onMessageRowClicked(position);
            }
        });

        holder.messageContainer.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                listener.onRowLongClicked(position);
                view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
                return true;
            }
        });
    }
    private void applyProfilePicture(MyViewHolder holder, AssociatedParentDTO message) {
      /*  if (!TextUtils.isEmpty(message.getPicture())) {
            Glide.with(mContext)
                    .load(message.getPicture())
                    .apply(new RequestOptions()
                            .placeholder(R.mipmap.ic_launcher_round)
                            .centerCrop()
                            .transform(new CircleTransform(mContext))
                            .dontAnimate()
                            .dontTransform())
                    .into(holder.imgProfile);


            holder.imgProfile.setColorFilter(null);
            holder.iconText.setVisibility(View.GONE);
        } else {*/
        holder.imgProfile.setImageResource(R.drawable.bg_circle);
        holder.imgProfile.setColorFilter(getRandomMaterialColor("400"));
        holder.iconText.setVisibility(View.VISIBLE);
        // }
    }



    public  int getRandomMaterialColor(String typeColor) {
        int returnColor = Color.GRAY;
        int arrayId = mContext.getResources().getIdentifier("mdcolor_" + typeColor, "array", mContext.getPackageName());

        if (arrayId != 0) {
            TypedArray colors = mContext.getResources().obtainTypedArray(arrayId);
            int index = (int) (Math.random() * colors.length());
            returnColor = colors.getColor(index, Color.GRAY);
            colors.recycle();
        }
        return returnColor;
    }

    private void applyIconAnimation(MyViewHolder holder, int position) {
        if (selectedItems.get(position, false)) {
            holder.iconFront.setVisibility(View.GONE);
            resetIconYAxis(holder.iconBack);
            holder.iconBack.setVisibility(View.VISIBLE);
            holder.iconBack.setAlpha(1);
            if (currentSelectedIndex == position) {
                FlipAnimator.flipView(mContext, holder.iconBack, holder.iconFront, true);
                resetCurrentIndex();
            }
        } else {
            holder.iconBack.setVisibility(View.GONE);
            resetIconYAxis(holder.iconFront);
            holder.iconFront.setVisibility(View.VISIBLE);
            holder.iconFront.setAlpha(1);
            if ((reverseAllAnimations && animationItemsIndex.get(position, false)) || currentSelectedIndex == position) {
                FlipAnimator.flipView(mContext, holder.iconBack, holder.iconFront, false);
                resetCurrentIndex();
            }
        }
    }


    // As the views will be reused, sometimes the icon appears as
    // flipped because older view is reused. Reset the Y-axis to 0
    private void resetIconYAxis(View view) {
        if (view.getRotationY() != 0) {
            view.setRotationY(0);
        }
    }

    public void resetAnimationIndex() {
        reverseAllAnimations = false;
        animationItemsIndex.clear();
    }





 /*  @Override
    public long getItemId(int position) {
        return parentListData.get(position);
    }*/

//    private void applyImportant(MyViewHolder holder, InvoiceDTO message) {
//        if (message.isImportant()) {
//            holder.iconImp.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_star_black_24dp));
//            holder.iconImp.setColorFilter(ContextCompat.getColor(mContext, R.color.icon_tint_selected));
//        } else {
//            holder.iconImp.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_star_border_black_24dp));
//            holder.iconImp.setColorFilter(ContextCompat.getColor(mContext, R.color.icon_tint_normal));
//        }
//    }

  /*  private void applyReadStatus(MyViewHolder holder, InvoiceDTO message) {
        if (message.isRead()) {
            holder.from.setTypeface(null, Typeface.NORMAL);
            holder.subject.setTypeface(null, Typeface.NORMAL);
            holder.from.setTextColor(ContextCompat.getColor(mContext, R.color.subject));
            holder.subject.setTextColor(ContextCompat.getColor(mContext, R.color.message));
        } else {
            holder.from.setTypeface(null, Typeface.BOLD);
            holder.subject.setTypeface(null, Typeface.BOLD);
            holder.from.setTextColor(ContextCompat.getColor(mContext, R.color.from));
            holder.subject.setTextColor(ContextCompat.getColor(mContext, R.color.subject));
        }
    }
*/
    @Override
    public int getItemCount() {
        return parentListData.size();
    }

    public void toggleSelection(int pos) {
        currentSelectedIndex = pos;
        if (selectedItems.get(pos, false)) {
            selectedItems.delete(pos);
            animationItemsIndex.delete(pos);
        } else {
            selectedItems.put(pos, true);
            animationItemsIndex.put(pos, true);
        }
        notifyItemChanged(pos);
    }

    public void clearSelections() {
        reverseAllAnimations = true;
        selectedItems.clear();
        notifyDataSetChanged();
    }

    public int getSelectedItemCount() {
        return selectedItems.size();
    }

    public List<Integer> getSelectedItems() {
        List<Integer> items =
                new ArrayList<>(selectedItems.size());
        for (int i = 0; i < selectedItems.size(); i++) {
            items.add(selectedItems.keyAt(i));
        }
        return items;
    }




    public void removeData(int position) {
        parentListData.remove(position);
        resetCurrentIndex();
    }

    private void resetCurrentIndex() {
        currentSelectedIndex = -1;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        public TextView txt_parentname,txt_total_child, txt_address,  iconText;
        public ImageView imgProfile;
        public LinearLayout messageContainer;
        public RelativeLayout iconContainer, iconBack, iconFront;

        public MyViewHolder(View view) {
            super(view);
            txt_parentname = (TextView) view.findViewById(R.id.txt_parentname);
            txt_total_child = (TextView) view.findViewById(R.id.txt_total_child);
            iconText = (TextView) view.findViewById(R.id.icon_text);

            iconBack = (RelativeLayout) view.findViewById(R.id.icon_back);
            iconFront = (RelativeLayout) view.findViewById(R.id.icon_front);
            imgProfile = (ImageView) view.findViewById(R.id.icon_profile);
            messageContainer = (LinearLayout) view.findViewById(R.id.message_container);
            iconContainer = (RelativeLayout) view.findViewById(R.id.icon_container);
            view.setOnLongClickListener(this);
        }

        @Override
        public boolean onLongClick(View view) {
            listener.onRowLongClicked(getAdapterPosition());
            view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
            return true;
        }
    }



    public interface RecyleAdapterListener {
        void onIconClicked(int position);

        void onIconImportantClicked(int position);

        void onMessageRowClicked(int position);

        void onRowLongClicked(int position);
    }
}